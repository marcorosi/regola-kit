package org.regola.formsValidation;

import java.beans.Introspector;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.AssertionFailure;
import org.hibernate.Hibernate;
import org.hibernate.MappingException;
import org.hibernate.annotations.common.reflection.Filter;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XMember;
import org.hibernate.annotations.common.reflection.XMethod;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.annotations.common.reflection.java.JavaReflectionManager;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.util.IdentitySet;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.MessageInterpolator;
import org.hibernate.validator.PersistentClassConstraint;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Valid;
import org.hibernate.validator.Validator;
import org.hibernate.validator.ValidatorClass;
import org.hibernate.validator.interpolator.DefaultMessageInterpolatorAggerator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/*
 * Adapted from org.hibernate.validator.ClassValidator
 */
public class AmendmentsClassValidator<T> implements Serializable 
{
	private static final long serialVersionUID = -5071107234712195025L;
	
	protected List<Amendment> amendments = new ArrayList<Amendment>();//MY
	protected Annotation[] permittedAmendmentAddTypes; //MY
	
	private static Log log = LogFactory.getLog( AmendmentsClassValidator.class );
	private static final InvalidValue[] EMPTY_INVALID_VALUE_ARRAY = new InvalidValue[]{};
	private static final String DEFAULT_VALIDATOR_MESSAGE = "org.hibernate.validator.resources.DefaultValidatorMessages";
	private static final String VALIDATOR_MESSAGE = "ValidatorMessages";
	private static final Set<Class> INDEXABLE_CLASS = new HashSet<Class>();
	
	private static final String EMENDAMENT_ADD = "add";
	private static final String EMENDAMENT_REMOVE = "remove";

	static {
		INDEXABLE_CLASS.add( Integer.class );
		INDEXABLE_CLASS.add( Long.class );
		INDEXABLE_CLASS.add( String.class );
	}

	/*
	static {
		Version.touch(); //touch version
	}
	*/

	private final Class<T> beanClass;
	private transient ResourceBundle messageBundle;
	private transient ResourceBundle defaultMessageBundle;
	private transient boolean isUserProvidedResourceBundle;
	private transient ReflectionManager reflectionManager;

	private final transient Map<XClass, AmendmentsClassValidator> childClassValidators;
	private transient List<Validator> beanValidators;
	private transient List<Validator> memberValidators;
	private transient List<XMember> memberGetters;
	private transient List<XMember> childGetters;
	private transient DefaultMessageInterpolatorAggerator defaultInterpolator;
	private transient MessageInterpolator userInterpolator;
	
	
	private static final Filter GET_ALL_FILTER = new Filter() {
		public boolean returnStatic() {
		return true;
		}

		public boolean returnTransient() {
		return true;
		}
	};
	
	//==========================================================================================================
	//----costruttori con parametro path alla configurazione degli emendamenti
	//==========================================================================================================
	/**
	 * create the validator engine for this bean type
	 */
	public AmendmentsClassValidator(Class<T> beanClass) {
		this( beanClass, (ResourceBundle) null, (String) null );
	}
	
	public AmendmentsClassValidator(Class<T> beanClass , String validationAmendmentsCfgFile) {
		this( beanClass, (ResourceBundle) null,  validationAmendmentsCfgFile);
	}	

	/**
	 * create the validator engine for a particular bean class, using a resource bundle
	 * for message rendering on violation
	 */
	public AmendmentsClassValidator(Class<T> beanClass, ResourceBundle resourceBundle, String validationAmendmentsCfgFile) {
		this( beanClass, resourceBundle, null, new HashMap<XClass, AmendmentsClassValidator>(), null, validationAmendmentsCfgFile );
	}

	/**
	 * create the validator engine for a particular bean class, using a custom message interpolator
	 * for message rendering on violation
	 */
	public AmendmentsClassValidator(Class<T> beanClass, MessageInterpolator interpolator) {
		this( beanClass, null, interpolator, new HashMap<XClass, AmendmentsClassValidator>(), null, (String) null );
	}

    /**
     * Not a public API
     */
    public AmendmentsClassValidator(
			Class<T> beanClass, ResourceBundle resourceBundle, MessageInterpolator interpolator,
            Map<XClass, AmendmentsClassValidator> childClassValidators, ReflectionManager reflectionManager, 
            String validationAmendmentsCfgFile
    ) {
        this.reflectionManager = reflectionManager != null ? reflectionManager : new JavaReflectionManager();
        XClass beanXClass = this.reflectionManager.toXClass( beanClass );
		this.beanClass = beanClass;
		this.messageBundle = resourceBundle == null ?
				getDefaultResourceBundle() :
				resourceBundle;
		this.defaultMessageBundle = ResourceBundle.getBundle( DEFAULT_VALIDATOR_MESSAGE );
		this.userInterpolator = interpolator;
		this.childClassValidators = childClassValidators != null ?
                childClassValidators :
                new HashMap<XClass, AmendmentsClassValidator>();
		if(validationAmendmentsCfgFile != null && !validationAmendmentsCfgFile.trim().equals(""))
		{
			//amendments = readDSLEmendaments(validationAmendmentsCfgFile); //MY
			amendments = new AmendmentUtils().readDSLAmendments(validationAmendmentsCfgFile, beanClass);
			permittedAmendmentAddTypes = AmendmentUtils.getPermittedAddTypes();
		}
		initValidator( beanXClass, this.childClassValidators );
	}

	@SuppressWarnings("unchecked")
	protected AmendmentsClassValidator(
			XClass beanXClass, ResourceBundle resourceBundle, MessageInterpolator userInterpolator,
			Map<XClass, AmendmentsClassValidator> childClassValidators, ReflectionManager reflectionManager, 
			String validationAmendmentsCfgFile
	) {
		this.reflectionManager = reflectionManager;
		this.beanClass = reflectionManager.toClass( beanXClass );
		this.messageBundle = resourceBundle == null ?
				getDefaultResourceBundle() :
				resourceBundle;
		this.defaultMessageBundle = ResourceBundle.getBundle( DEFAULT_VALIDATOR_MESSAGE );
		this.userInterpolator = userInterpolator;
		this.childClassValidators = childClassValidators;
		if(validationAmendmentsCfgFile != null && !validationAmendmentsCfgFile.trim().equals(""))
		{
			//amendments = readDSLEmendaments(validationAmendmentsCfgFile); //MY
			amendments = new AmendmentUtils().readDSLAmendments(validationAmendmentsCfgFile, beanClass);
			permittedAmendmentAddTypes = AmendmentUtils.getPermittedAddTypes();
		}
		initValidator( beanXClass, childClassValidators );
	}
	
	//==========================================================================================================
	//----costruttori con parametro List di emendamenti
	//==========================================================================================================
	/**
	 * create the validator engine for this bean type
	 */
	public AmendmentsClassValidator(Class<T> beanClass , List<Amendment> amendments) {
		this( beanClass, (ResourceBundle) null,  amendments);
	}	

	/**
	 * create the validator engine for a particular bean class, using a resource bundle
	 * for message rendering on violation
	 */
	public AmendmentsClassValidator(Class<T> beanClass, ResourceBundle resourceBundle, List<Amendment> amendments) {
		this( beanClass, resourceBundle, null, new HashMap<XClass, AmendmentsClassValidator>(), null, amendments );
	}

    /**
     * Not a public API
     */
    public AmendmentsClassValidator(
			Class<T> beanClass, ResourceBundle resourceBundle, MessageInterpolator interpolator,
            Map<XClass, AmendmentsClassValidator> childClassValidators, ReflectionManager reflectionManager, 
            List<Amendment> amendments
    ) {
        this.reflectionManager = reflectionManager != null ? reflectionManager : new JavaReflectionManager();
        XClass beanXClass = this.reflectionManager.toXClass( beanClass );
		this.beanClass = beanClass;
		this.messageBundle = resourceBundle == null ?
				getDefaultResourceBundle() :
				resourceBundle;
		this.defaultMessageBundle = ResourceBundle.getBundle( DEFAULT_VALIDATOR_MESSAGE );
		this.userInterpolator = interpolator;
		this.childClassValidators = childClassValidators != null ?
                childClassValidators :
                new HashMap<XClass, AmendmentsClassValidator>();
		this.amendments = amendments;
		permittedAmendmentAddTypes = AmendmentUtils.getPermittedAddTypes();
		initValidator( beanXClass, this.childClassValidators );
	}

	@SuppressWarnings("unchecked")
	protected AmendmentsClassValidator(
			XClass beanXClass, ResourceBundle resourceBundle, MessageInterpolator userInterpolator,
			Map<XClass, AmendmentsClassValidator> childClassValidators, ReflectionManager reflectionManager, 
			List<Amendment> amendments
	) {
		this.reflectionManager = reflectionManager;
		this.beanClass = reflectionManager.toClass( beanXClass );
		this.messageBundle = resourceBundle == null ?
				getDefaultResourceBundle() :
				resourceBundle;
		this.defaultMessageBundle = ResourceBundle.getBundle( DEFAULT_VALIDATOR_MESSAGE );
		this.userInterpolator = userInterpolator;
		this.childClassValidators = childClassValidators;
		this.amendments = amendments;
		permittedAmendmentAddTypes = AmendmentUtils.getPermittedAddTypes();
		initValidator( beanXClass, childClassValidators );
	}	
	
	/*
	 * logica spostata in AmendmentsUtils
	 * 
	protected List<Amendment> readDSLEmendaments(String validationAmendmentsCfgFile) { //MY
		//caricamento dsl emendamenti
		
		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		xstream.alias("AmendedModelClass", AmendedModelClass.class);
		xstream.alias("Amendment", Amendment.class);
				
		URL url = getClass().getClassLoader().getResource(validationAmendmentsCfgFile);
		
		try {
			List<AmendedModelClass> emendedClasses = (List<AmendedModelClass>)xstream.fromXML(new FileReader(url.getFile()));
			return retrieveEmendaments(emendedClasses);
		} catch (Exception e) {
			//throw new RuntimeException(e);
			log.error("Errore nella lettura del file di configurazione degli emendamenti: " + validationAmendmentsCfgFile);
			return new ArrayList<Amendment>();
		}
	}
	
	protected List<Amendment> retrieveEmendaments(List<AmendedModelClass> emendedModelClasses)
	{
		List<Amendment> emendaments = new ArrayList<Amendment>();
		for (AmendedModelClass emendedClass : emendedModelClasses)
		{
			if( beanClass.getCanonicalName().equals(emendedClass.getModelClass()) )
				emendaments.addAll(emendedClass.getAmendments());
		}
		return emendaments;
	}
	*/

	private ResourceBundle getDefaultResourceBundle() {
		ResourceBundle rb;
		try {
			//use context class loader as a first citizen
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if ( contextClassLoader == null ) {
				throw new MissingResourceException( "No context classloader", null, VALIDATOR_MESSAGE );
			}
			rb = ResourceBundle.getBundle(
					VALIDATOR_MESSAGE,
					Locale.getDefault(),
					contextClassLoader
			);
		}
		catch (MissingResourceException e) {
			log.trace( "ResourceBundle " + VALIDATOR_MESSAGE + " not found in thread context classloader" );
			//then use the Validator Framework classloader
			try {
				rb = ResourceBundle.getBundle(
						VALIDATOR_MESSAGE,
						Locale.getDefault(),
						this.getClass().getClassLoader()
				);
			}
			catch (MissingResourceException ee) {
				log.debug(
						"ResourceBundle ValidatorMessages not found in Validator classloader. Delegate to " + DEFAULT_VALIDATOR_MESSAGE
				);
				//the user did not override the default ValidatorMessages
				rb = null;
			}
		}
		isUserProvidedResourceBundle = true;
		return rb;
	}

	private void initValidator(
			XClass xClass, Map<XClass, AmendmentsClassValidator> childClassValidators
	) {
		beanValidators = new ArrayList<Validator>();
		memberValidators = new ArrayList<Validator>();
		memberGetters = new ArrayList<XMember>();
		childGetters = new ArrayList<XMember>();
		defaultInterpolator = new DefaultMessageInterpolatorAggerator();
		defaultInterpolator.initialize( messageBundle, defaultMessageBundle );

		//build the class hierarchy to look for members in
		childClassValidators.put( xClass, this );
		Collection<XClass> classes = new HashSet<XClass>();
		addSuperClassesAndInterfaces( xClass, classes );
		for ( XClass currentClass : classes ) {
			Annotation[] classAnnotations = currentClass.getAnnotations();
			for ( int i = 0; i < classAnnotations.length ; i++ ) {
				Annotation classAnnotation = classAnnotations[i];
				Validator beanValidator = createValidator( classAnnotation );
				if ( beanValidator != null ) beanValidators.add( beanValidator );
				handleAggregateAnnotations(classAnnotation, null);
			}
		}

		//Check on all selected classes
		for ( XClass currClass : classes ) {
			List<XMethod> methods = currClass.getDeclaredMethods();
			for ( XMethod method : methods ) {
				createMemberValidator( method );
				createChildValidator( method );
			}

			List<XProperty> fields = currClass.getDeclaredProperties(
					"field", GET_ALL_FILTER
			);
			for ( XProperty field : fields ) {
				createMemberValidator( field );
				createChildValidator( field );
			}
		}
	}

	private void addSuperClassesAndInterfaces(XClass clazz, Collection<XClass> classes) {
		for ( XClass currClass = clazz; currClass != null ; currClass = currClass.getSuperclass() ) {
			if ( ! classes.add( currClass ) ) return;
			XClass[] interfaces = currClass.getInterfaces();
			for ( XClass interf : interfaces ) {
				addSuperClassesAndInterfaces( interf, classes );
			}
		}
	}

	private boolean handleAggregateAnnotations(Annotation annotation, XMember member) {
		Object[] values;
		try {
			Method valueMethod = annotation.getClass().getMethod( "value" );
			if ( valueMethod.getReturnType().isArray() ) {
				values = (Object[]) valueMethod.invoke( annotation );
			}
			else {
				return false;
			}
		}
		catch (NoSuchMethodException e) {
			return false;
		}
		catch (Exception e) {
			throw new IllegalStateException( e );
		}

		boolean validatorPresent = false;
		for ( Object value : values ) {
			if ( value instanceof Annotation ) {
				annotation = (Annotation) value;
				
				Iterator iterator = amendments.iterator();
				boolean found = false;
				Amendment emendament = null;
				while(!found && iterator.hasNext())
				{
					emendament = (Amendment)iterator.next();
					String memberName = member.getName().replace("set", "").replace("get", "");
					if( annotation.annotationType().getCanonicalName().equals(emendament.getValidationType()) 
							&& memberName.equalsIgnoreCase(emendament.getModelProperty()))
						found = true;
				}
				if( !found || ( found && !emendament.getAmendmentType().equals(EMENDAMENT_REMOVE)) )
				{				
				
					Validator validator = createValidator( annotation );
					if ( validator != null ) {
						if ( member != null ) {
							//member
							memberValidators.add( validator );
							setAccessible( member );
							memberGetters.add( member );
						}
						else {
							//bean
							beanValidators.add( validator );
						}
						validatorPresent = true;
					}
				
				}			
				
			}
		}
		return validatorPresent;
	}

	@SuppressWarnings("unchecked")
	private void createChildValidator( XMember member) {
		if ( member.isAnnotationPresent( Valid.class ) ) {
			setAccessible( member );
			childGetters.add( member );
			XClass clazz;
			if ( member.isCollection() || member.isArray() ) {
				clazz = member.getElementClass();
			}
			else {
				clazz = member.getType();
			}
			if ( !childClassValidators.containsKey( clazz ) ) {
				//ClassValidator added by side effect (added to childClassValidators during CV construction)
				new AmendmentsClassValidator( clazz, messageBundle, userInterpolator, childClassValidators, reflectionManager, (String)null );
			}
		}
	}

	private void createMemberValidator(XMember member) {
		boolean validatorPresent = false;
		Annotation[] memberAnnotations = member.getAnnotations();
		String memberName = member.getName().replace("get", "").replace("set", "");//fabio
		for ( Annotation methodAnnotation : memberAnnotations ) 
		{
			
			Iterator iterator = amendments.iterator();
			boolean found = false;
			Amendment emendament = null;
			while(!found && iterator.hasNext())
			{
				emendament = (Amendment)iterator.next();
				//N.B. XMember è un'astrazione sia per i methods che per i fields
				if( methodAnnotation.annotationType().getCanonicalName().equals(emendament.getValidationType()) 
						&& memberName.equalsIgnoreCase(emendament.getModelProperty()) )
					found = true;
			}
			if( !found || ( found && !emendament.getAmendmentType().equals(EMENDAMENT_REMOVE)) )
			{		
				
				Validator propertyValidator = createValidator( methodAnnotation );
				if ( propertyValidator != null ) {
					memberValidators.add( propertyValidator );
					setAccessible( member );
					memberGetters.add( member );
					validatorPresent = true;
					cleanAmendments(methodAnnotation, memberName); //rimuove un eventuale emendamento di aggiunta dello stesso tipo
				}
				boolean agrValidPresent = handleAggregateAnnotations( methodAnnotation, member );
				validatorPresent = validatorPresent || agrValidPresent;
			
			}
		}//for memberAnnotations
		
		//Aggiungo gli eventuali validatori dati dalla configurazione DSL degli emendamenti
		for (Amendment amendment : amendments)
		{
			if(amendment.getModelProperty().equals(memberName)
					&& amendment.getAmendmentType().equals(EMENDAMENT_ADD))
			{
				
				try
				{
					Annotation annotation = getAnnotationToAdd(amendment.getValidationType());
					
					if(annotation != null) //l'annotazione è tra quelle permesse per gli emendamenti di aggiunta
					{
						Validator propertyValidator = createValidator( annotation );
						if ( propertyValidator != null ) 
						{
							memberValidators.add( propertyValidator );
							setAccessible( member );
							memberGetters.add( member );
							validatorPresent = true;
						}
					}else
						log.error("Non è possibile aggiungere il tipo di validazione " + amendment.getValidationType() + " tramite emendamenti!");
					boolean agrValidPresent = handleAggregateAnnotations( annotation, member );
					validatorPresent = validatorPresent || agrValidPresent;					
				} catch (Exception e)
				{
					log.error("impossibile creare un Validator per l'annotazione di tipo " + amendment.getValidationType() );
				}
			}
		}
		
		if ( validatorPresent && !member.isTypeResolved() ) {
			log.warn( "Original type of property " + member + " is unbound and has been approximated." );
		}
	}
	
	private void cleanAmendments(Annotation annotation, String modelProperty)
	{
		for(Iterator iter = amendments.iterator(); iter.hasNext();)
		{
			Amendment amendment = (Amendment)iter.next();
			if(amendment.getValidationType().equals(annotation.annotationType().getCanonicalName())
					&& EMENDAMENT_ADD.equals(amendment.getAmendmentType())
					&& amendment.getModelProperty().equalsIgnoreCase(modelProperty))
			{
				log.warn("Validazione " + annotation.annotationType().getCanonicalName() + " già specificata come annotazione!");
				iter.remove();
			}
		}
	}
	
	protected Annotation getAnnotationToAdd(String annotationType)
	{
		for (int i = 0; i < permittedAmendmentAddTypes.length; i++)
		{
			if(annotationType.equals(permittedAmendmentAddTypes[i].annotationType().getCanonicalName()))
				return permittedAmendmentAddTypes[i];
		}
		return null;
	}

	private static void setAccessible(XMember member) {
		if ( !Modifier.isPublic( member.getModifiers() ) ) {
			member.setAccessible( true );
		}
	}

	@SuppressWarnings("unchecked")
	private Validator createValidator(Annotation annotation) {
		try {
			ValidatorClass validatorClass = annotation.annotationType().getAnnotation( ValidatorClass.class );
			if ( validatorClass == null ) {
				return null;
			}
			Validator beanValidator = validatorClass.value().newInstance();
			beanValidator.initialize( annotation );
			defaultInterpolator.addInterpolator( annotation, beanValidator );
			return beanValidator;
		}
		catch (Exception e) {
			throw new IllegalArgumentException( "could not instantiate EmendamentsClassValidator", e );
		}
	}

	public boolean hasValidationRules() {
		return beanValidators.size() != 0 || memberValidators.size() != 0;
	}

	/**
	 * apply constraints on a bean instance and return all the failures.
	 * if <code>bean</code> is null, an empty array is returned
	 */
	public InvalidValue[] getInvalidValues(T bean) {
		return this.getInvalidValues( bean, new IdentitySet() );
	}

	/**
	 * apply constraints on a bean instance and return all the failures.
	 * if <code>bean</code> is null, an empty array is returned
	 */
	@SuppressWarnings("unchecked")
	protected InvalidValue[] getInvalidValues(T bean, Set<Object> circularityState) {
		if ( bean == null || circularityState.contains( bean ) ) {
			return EMPTY_INVALID_VALUE_ARRAY; //Avoid circularity
		}
		else {
			circularityState.add( bean );
		}

		if ( !beanClass.isInstance( bean ) ) {
			throw new IllegalArgumentException( "not an instance of: " + bean.getClass() );
		}

		List<InvalidValue> results = new ArrayList<InvalidValue>();

		for ( int i = 0; i < beanValidators.size() ; i++ ) {
			Validator validator = beanValidators.get( i );
			if ( !validator.isValid( bean ) ) {
				results.add( new InvalidValue( interpolate(validator), beanClass, null, bean, bean ) );
			}
		}

		for ( int i = 0; i < memberValidators.size() ; i++ ) {
			XMember getter = memberGetters.get( i );
			if ( Hibernate.isPropertyInitialized( bean, getPropertyName( getter ) ) ) {
				Object value = getMemberValue( bean, getter );
				Validator validator = memberValidators.get( i );
				if ( !validator.isValid( value ) ) {
					String propertyName = getPropertyName( getter );
					results.add( new InvalidValue( interpolate(validator), beanClass, propertyName, value, bean ) );
				}
			}
		}

		for ( int i = 0; i < childGetters.size() ; i++ ) {
			XMember getter = childGetters.get( i );
			if ( Hibernate.isPropertyInitialized( bean, getPropertyName( getter ) ) ) {
				Object value = getMemberValue( bean, getter );
				if ( value != null && Hibernate.isInitialized( value ) ) {
					String propertyName = getPropertyName( getter );
					if ( getter.isCollection() ) {
						int index = 0;
						boolean isIterable = value instanceof Iterable;
						Map map = ! isIterable ? (Map) value : null;
						Iterable elements = isIterable ?
								(Iterable) value :
								map.keySet();
						for ( Object element : elements ) {
							Object actualElement = isIterable ? element : map.get( element );
							if ( actualElement == null ) {
								index++;
								continue;
							}
							InvalidValue[] invalidValues = getClassValidator( actualElement )
									.getInvalidValues( actualElement, circularityState );

							String indexedPropName = MessageFormat.format(
									"{0}[{1}]",
									propertyName,
									INDEXABLE_CLASS.contains( element.getClass() ) ?
											( "'" + element + "'" ) :
											index
							);
							index++;

							for ( InvalidValue invalidValue : invalidValues ) {
								invalidValue.addParentBean( bean, indexedPropName );
								results.add( invalidValue );
							}
						}
					}
					if ( getter.isArray() ) {
						int index = 0;
						for ( Object element : (Object[]) value ) {
							if ( element == null ) {
								index++;
								continue;
							}
							InvalidValue[] invalidValues = getClassValidator( element )
									.getInvalidValues( element, circularityState );

							String indexedPropName = MessageFormat.format(
									"{0}[{1}]",
									propertyName,
									index
							);
							index++;

							for ( InvalidValue invalidValue : invalidValues ) {
								invalidValue.addParentBean( bean, indexedPropName );
								results.add( invalidValue );
							}
						}
					}
					else {
						InvalidValue[] invalidValues = getClassValidator( value )
								.getInvalidValues( value, circularityState );
						for ( InvalidValue invalidValue : invalidValues ) {
							invalidValue.addParentBean( bean, propertyName );
							results.add( invalidValue );
						}
					}
				}
			}
		}

		return results.toArray( new InvalidValue[results.size()] );
	}

	private String interpolate(Validator validator) {
		String message = defaultInterpolator.getAnnotationMessage( validator );
		if (userInterpolator != null) {
			return userInterpolator.interpolate( message, validator, defaultInterpolator );
		}
		else {
			return defaultInterpolator.interpolate( message, validator, null);
		}
	}

	@SuppressWarnings("unchecked")
	private AmendmentsClassValidator getClassValidator(Object value) {
		Class clazz = value.getClass();
		AmendmentsClassValidator validator = childClassValidators.get( reflectionManager.toXClass( clazz ) );
		if ( validator == null ) { //handles polymorphism
			validator = new AmendmentsClassValidator( clazz );
		}
		return validator;
	}

	/**
	 * Apply constraints of a particular property on a bean instance and return all the failures.
	 * Note this is not recursive.
	 */
	//TODO should it be recursive?
	public InvalidValue[] getInvalidValues(T bean, String propertyName) {
		List<InvalidValue> results = new ArrayList<InvalidValue>();

		for ( int i = 0; i < memberValidators.size() ; i++ ) {
			XMember getter = memberGetters.get( i );
			if ( getPropertyName( getter ).equals( propertyName ) ) {
				Object value = getMemberValue( bean, getter );
				Validator validator = memberValidators.get( i );
				if ( !validator.isValid( value ) ) {
					results.add( new InvalidValue( interpolate(validator), beanClass, propertyName, value, bean ) );
				}
			}
		}

		return results.toArray( new InvalidValue[results.size()] );
	}

	/**
	 * Apply constraints of a particular property value of a bean type and return all the failures.
	 * The InvalidValue objects returns return null for InvalidValue#getBean() and InvalidValue#getRootBean()
	 * Note this is not recursive.
	 */
	//TODO should it be recursive?
	public InvalidValue[] getPotentialInvalidValues(String propertyName, Object value) {
		List<InvalidValue> results = new ArrayList<InvalidValue>();

		for ( int i = 0; i < memberValidators.size() ; i++ ) {
			XMember getter = memberGetters.get( i );
			if ( getPropertyName( getter ).equals( propertyName ) ) {
				Validator validator = memberValidators.get( i );
				if ( !validator.isValid( value ) ) {
					results.add( new InvalidValue( interpolate(validator), beanClass, propertyName, value, null ) );
				}
			}
		}

		return results.toArray( new InvalidValue[results.size()] );
	}

	private Object getMemberValue(T bean, XMember getter) {
		Object value;
		try {
			value = getter.invoke( bean );
		}
		catch (Exception e) {
			throw new IllegalStateException( "Could not get property value", e );
		}
		return value;
	}

	public String getPropertyName(XMember member) {
		//Do no try to cache the result in a map, it's actually much slower (2.x time)
		String propertyName;
		if ( XProperty.class.isAssignableFrom( member.getClass() ) ) {
			propertyName = member.getName();
		}
		else if ( XMethod.class.isAssignableFrom( member.getClass() ) ) {
			propertyName = member.getName();
			if ( propertyName.startsWith( "is" ) ) {
				propertyName = Introspector.decapitalize( propertyName.substring( 2 ) );
			}
			else if ( propertyName.startsWith( "get" ) ) {
				propertyName = Introspector.decapitalize( propertyName.substring( 3 ) );
			}
			//do nothing for non getter method, in case someone want to validate a PO Method
		}
		else {
			throw new AssertionFailure( "Unexpected member: " + member.getClass().getName() );
		}
		return propertyName;
	}

	/** @deprecated */
	private String replace(String message, Annotation parameters) {
		StringTokenizer tokens = new StringTokenizer( message, "#{}", true );
		StringBuilder buf = new StringBuilder( 30 );
		boolean escaped = false;
		boolean el = false;
		while ( tokens.hasMoreTokens() ) {
			String token = tokens.nextToken();
			if ( !escaped && "#".equals( token ) ) {
				el = true;
			}
			if ( !el && "{".equals( token ) ) {
				escaped = true;
			}
			else if ( escaped && "}".equals( token ) ) {
				escaped = false;
			}
			else if ( !escaped ) {
				if ( "{".equals( token ) ) el = false;
				buf.append( token );
			}
			else {
				Method member;
				try {
					member = parameters.getClass().getMethod( token, (Class[]) null );
				}
				catch (NoSuchMethodException nsfme) {
					member = null;
				}
				if ( member != null ) {
					try {
						buf.append( member.invoke( parameters ) );
					}
					catch (Exception e) {
						throw new IllegalArgumentException( "could not render message", e );
					}
				}
				else {
					String string = null;
					try {
						string = messageBundle != null ? messageBundle.getString( token ) : null;
					}
					catch( MissingResourceException e ) {
						//give a second chance with the default resource bundle
					}
					if (string == null) {
						try {
							string = defaultMessageBundle.getString( token );
						}
						catch( MissingResourceException e) {
							throw new MissingResourceException(
									"Can't find resource in validator bundles, key " + token,
                                    defaultMessageBundle.getClass().getName(),
                                    token
							);
						}
					}
					if ( string != null ) buf.append( replace( string, parameters ) );
				}
			}
		}
		return buf.toString();
	}

	/**
	 * apply the registred constraints rules on the hibernate metadata (to be applied on DB schema...)
	 *
	 * @param persistentClass hibernate metadata
	 */
	public void apply(PersistentClass persistentClass) {

		for ( Validator validator : beanValidators ) {
			if ( validator instanceof PersistentClassConstraint ) {
				( (PersistentClassConstraint) validator ).apply( persistentClass );
			}
		}

		Iterator<Validator> validators = memberValidators.iterator();
		Iterator<XMember> getters = memberGetters.iterator();
		while ( validators.hasNext() ) {
			Validator validator = validators.next();
			String propertyName = getPropertyName( getters.next() );
			if ( validator instanceof PropertyConstraint ) {
				try {
					Property property = findPropertyByName(persistentClass, propertyName);
					if (property != null) {
						( (PropertyConstraint) validator ).apply( property );
					}
				}
				catch (MappingException pnfe) {
					//do nothing
				}
			}
		}

	}

	public void assertValid(T bean) {
		InvalidValue[] values = getInvalidValues( bean );
		if ( values.length > 0 ) {
			throw new InvalidStateException( values );
		}
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		ResourceBundle rb = messageBundle;
		MessageInterpolator interpolator = this.userInterpolator;
		if ( rb != null && ! ( rb instanceof Serializable ) ) {
			messageBundle = null;
			if ( ! isUserProvidedResourceBundle ) {
				log.warn(
						"Serializing an EmendamentsClassValidator with a non serializable ResourceBundle: ResourceBundle ignored"
				);
			}
		}
		if (interpolator != null && ! (interpolator instanceof Serializable) ) {
			userInterpolator = null;
			log.warn( "Serializing a non serializable MessageInterpolator" );
		}
		oos.defaultWriteObject();
		oos.writeObject( messageBundle );
		oos.writeObject( userInterpolator );
		messageBundle = rb;
		userInterpolator = interpolator;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		ResourceBundle rb = (ResourceBundle) ois.readObject();
		if ( rb == null ) rb = getDefaultResourceBundle();
		this.messageBundle = rb;
		this.userInterpolator = (MessageInterpolator) ois.readObject();
		this.defaultMessageBundle = ResourceBundle.getBundle( DEFAULT_VALIDATOR_MESSAGE );
		reflectionManager = new JavaReflectionManager();
		initValidator( reflectionManager.toXClass( beanClass ), new HashMap<XClass, AmendmentsClassValidator>() );
	}

	/**
	 * Retrieve the property by path in a recursive way, including IndetifierProperty in the loop
	 * If propertyName is null or empty, the IdentifierProperty is returned
	 */
	public static Property findPropertyByName(PersistentClass associatedClass, String propertyName) {
		Property property = null;
		Property idProperty = associatedClass.getIdentifierProperty();
		String idName = idProperty != null ? idProperty.getName() : null;
		try {
			if ( propertyName == null
					|| propertyName.length() == 0
					|| propertyName.equals( idName ) ) {
				//default to id
				property = idProperty;
			}
			else {
				if ( propertyName.indexOf( idName + "." ) == 0 ) {
					property = idProperty;
					propertyName = propertyName.substring( idName.length() + 1 );
				}
				StringTokenizer st = new StringTokenizer( propertyName, ".", false );
				while ( st.hasMoreElements() ) {
					String element = (String) st.nextElement();
					if ( property == null ) {
						property = associatedClass.getProperty( element );
					}
					else {
						if ( ! property.isComposite() ) return null;
						property = ( (Component) property.getValue() ).getProperty( element );
					}
				}
			}
		}
		catch (MappingException e) {
			try {
				//if we do not find it try to check the identifier mapper
				if ( associatedClass.getIdentifierMapper() == null ) return null;
				StringTokenizer st = new StringTokenizer( propertyName, ".", false );
				while ( st.hasMoreElements() ) {
					String element = (String) st.nextElement();
					if ( property == null ) {
						property = associatedClass.getIdentifierMapper().getProperty( element );
					}
					else {
						if ( ! property.isComposite() ) return null;
						property = ( (Component) property.getValue() ).getProperty( element );
					}
				}
			}
			catch (MappingException ee) {
				return null;
			}
		}
		return property;
	}

}
