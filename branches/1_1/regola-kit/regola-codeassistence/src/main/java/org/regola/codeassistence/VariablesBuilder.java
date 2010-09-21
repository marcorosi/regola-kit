package org.regola.codeassistence;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.regola.descriptor.IClassDescriptor;
import org.regola.descriptor.IPropertyDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import ognl.Ognl;

/**
 * @author nicola
 */
public class VariablesBuilder {
	// private IClassDescriptor descriptor;
	/**
	 * @uml.property name="parameters"
	 * @uml.associationEnd qualifier="key:java.lang.Object java.lang.Object"
	 */
	private Map<String, Object> parameters;

	@SuppressWarnings("unchecked")
	public VariablesBuilder(IClassDescriptor modelDescriptor,
			IClassDescriptor idDescriptor) {
		// this.descriptor = descriptor;
		parameters = new HashMap<String, Object>();

		parameters.put("tipo", modelDescriptor);
		parameters.put("tipoId", idDescriptor);

		parameters.put("package", getPackageName(modelDescriptor));
		parameters.put("dao_package", getPackageName(modelDescriptor) + ".dao");
		parameters.put("pattern_package", getPackageName(modelDescriptor)
				+ ".model.pattern");
		
		parameters.put("dao_impl_package", getPackageName(modelDescriptor)
				+ ".dao.hibernate");
		
		parameters.put("dao_mock_package", getPackageName(modelDescriptor)
				+ ".dao.mock");
		
		
		parameters.put("service_package", getPackageName(modelDescriptor)
				+ ".service");
		
		parameters.put("controller_package", getPackageName(modelDescriptor)
				+ ".controllers");
		
		parameters.put("service_impl_package", getPackageName(modelDescriptor)
				+ ".service.impl");
		parameters.put("mbean_package", getPackageName(modelDescriptor)
				+ ".webapp.action");

		parameters.put("model_class", modelDescriptor.getType().getName());
		parameters.put("model_name", modelDescriptor.getType().getSimpleName());
		
		parameters.put("model_package", modelDescriptor.getType().getPackage().getName());
		
		parameters.put("flow_name", Utils
				.lowerFirstLetter(getStringValue("model_name")));
		
		parameters.put("portlet_name", Utils
				.lowerFirstLetter(getStringValue("model_name"))+ "-portlet");
		
		parameters.put("id_class", modelDescriptor.getIdentifierDescriptor()
				.getPropertyType().getName());
		
		parameters.put("id_flex_class", modelDescriptor.getIdentifierDescriptor()
				.getFlexType());
		
		parameters.put("id_name", modelDescriptor.getIdentifierDescriptor()
				.getPropertyType().getSimpleName());

		parameters.put("dao_interface_name", modelDescriptor.getType()
				.getSimpleName()
				+ "Dao");

		parameters.put("mock_name", Utils.lowerFirstLetter(modelDescriptor
				.getType().getSimpleName()
				+ "Mock"));

		parameters.put("dao_interface_class", cat("dao_package",
				"dao_interface_name"));
		
		parameters.put("dao_impl_name", modelDescriptor.getType()
				.getSimpleName()
				+ "DaoHibernate");
		
		parameters.put("dao_mock_name", modelDescriptor.getType()
				.getSimpleName()
				+ "DaoMock");
		
		
		parameters.put("dao_impl_class", cat("dao_impl_package", "dao_name"));
		parameters.put("dao_bean_name",
				Utils.lowerFirstLetter((String) parameters
						.get("dao_interface_name")));

		parameters.put("service_interface_name", modelDescriptor.getType()
				.getSimpleName()
				+ "Manager");
		
		parameters.put("controller_name", modelDescriptor.getType()
				.getSimpleName()
				+ "Controller");
		
		parameters.put("service_interface_class", cat("service_package",
				"service_interface_name"));
		parameters.put("service_impl_name", modelDescriptor.getType()
				.getSimpleName()
				+ "Impl");
		parameters.put("service_impl_class", cat("service_impl_package",
				"service_name"));
		parameters.put("service_bean_name", Utils
				.lowerFirstLetter((String) parameters
						.get("service_interface_name")));

		parameters.put("service_interface_name", modelDescriptor.getType()
				.getSimpleName()
				+ "Manager");
		parameters.put("service_interface_class", cat("service_package",
				"service_interface_name"));
		parameters.put("service_impl_name", modelDescriptor.getType()
				.getSimpleName()
				+ "ManagerImpl");
		parameters.put("service_impl_class", cat("service_impl_package",
				"service_name"));
		parameters.put("service_bean_name", Utils
				.lowerFirstLetter((String) parameters
						.get("service_interface_name")));

		parameters.put("soap_service_name", modelDescriptor.getType()
				.getSimpleName()
				+ "Service");
		
		
		parameters.put("mbean_list_name", modelDescriptor.getType()
				.getSimpleName()
				+ "List");
		parameters.put("mbean_list_page", Utils.lowerFirstLetter(parameters
				.get("model_name")
				+ "-list.xhtml"));
		parameters.put("mbean_list_page_url", Utils.lowerFirstLetter(parameters
				.get("model_name")
				+ "-list.html"));

		parameters.put("mbean_form_name", modelDescriptor.getType()
				.getSimpleName()
				+ "Form");
		parameters.put("mbean_form_page", Utils.lowerFirstLetter(parameters
				.get("model_name")
				+ "-form.xhtml"));
		parameters.put("mbean_form_page_url", Utils.lowerFirstLetter(parameters
				.get("model_name")
				+ "-form.html"));

		parameters.put("filter_name", modelDescriptor.getType().getSimpleName()
				+ "Pattern");

		parameters.put("filter_class", cat("pattern_package", "filter_name"));

		parameters.put("field", new FieldNameConverter());
		parameters.put("same", new SameName());
		parameters.put("guessName", new NameGuesser());

		List<IPropertyDescriptor> idProperties = findProperties(idDescriptor);
		List<IPropertyDescriptor> modelProperties = findProperties(modelDescriptor);

		// List<IPropertyDescriptor> idProperties =
		// idDescriptor.getPropertyDescriptors();
		// List<IPropertyDescriptor> modelProperties =
		// modelDescriptor.getPropertyDescriptors();

		List<IPropertyDescriptor> collectionsProps = findCollectionsProperties(
				modelDescriptor, true);
		parameters.put("collectionsProps", collectionsProps);
		List<IPropertyDescriptor> notCollectionsProps = findCollectionsProperties(
				modelDescriptor, false);
		parameters.put("notCollectionsProps", notCollectionsProps);

		List<IPropertyDescriptor> allProperties = new ArrayList<IPropertyDescriptor>();
		allProperties.addAll(idProperties);
		allProperties.addAll(modelProperties);

		parameters.put("idProperties", idProperties);
		parameters.put("modelProperties", modelProperties);
		parameters.put("allProperties", allProperties);

		dumpVariables();
	}

	public Object getValue(String name) {
		return getParameters().get(name);
	}

	public String getStringValue(String name) {
		return getValue(name) == null ? "" : getValue(name).toString();
	}

	public void dumpVariables() {
		String[] keys = {};
		keys = parameters.keySet().toArray(keys);
		Arrays.sort(keys);
		for (String key : keys) {
			System.out.println(String.format("${%s}:%s", key, parameters
					.get(key)));
			// System.out.println(String.format("%s : ${%s}", key, key));
		}
	}

	@SuppressWarnings("unchecked")
	private List<IPropertyDescriptor> findProperties(
			IClassDescriptor modelDescriptor) {
		List<Class> annotazioni = new ArrayList<Class>();
		// annotazioni.add(Column.class);

		List<IPropertyDescriptor> properties = filterAnnotatedProperties(
				modelDescriptor, annotazioni);

		Iterator<IPropertyDescriptor> iter = properties.iterator();

		while (iter.hasNext()) {
			IPropertyDescriptor descriptor = iter.next();

			if (descriptor.getName().equals("id"))
				iter.remove();
			else if (descriptor.getPropertyType().getName().contains("$"))
				iter.remove();
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	private List<IPropertyDescriptor> findCollectionsProperties(
			IClassDescriptor modelDescriptor, boolean collections) {
		List<Class> annotazioni = new ArrayList<Class>();
		// annotazioni.add(Column.class);

		List<IPropertyDescriptor> properties = filterAnnotatedProperties(
				modelDescriptor, annotazioni);

		Iterator<IPropertyDescriptor> iter = properties.iterator();

		while (iter.hasNext()) {
			IPropertyDescriptor descriptor = iter.next();

			boolean isAColl = descriptor.isCollection()
					|| descriptor.getPropertyType().isArray();

			if (descriptor.getName().equals("id"))
				iter.remove();
			else if (descriptor.getPropertyType().getName().contains("$"))
				iter.remove();
			else if (isAColl != collections)
				iter.remove();

		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	private List<IPropertyDescriptor> filterAnnotatedProperties(
			IClassDescriptor descriptor, List<Class> annotations) {
		List<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();

		for (Iterator iter = descriptor.getPropertyDescriptors().iterator(); iter
				.hasNext();) {
			IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iter
					.next();
			filterPropertyDescriptor(propertyDescriptor, properties,
					annotations);
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	private void filterPropertyDescriptor(
			IPropertyDescriptor propertyDescriptor,
			List<IPropertyDescriptor> properties, List<Class> annotations) {
		IPropertyDescriptor clonedDescriptor = (IPropertyDescriptor) propertyDescriptor
				.clone();
		try {
			Field propertyField = clonedDescriptor.getBeanType()
					.getDeclaredField(propertyDescriptor.getName());
			if (couldAddProperty(annotations, propertyField.getAnnotations())) {
				properties.add(propertyDescriptor);
				return;
			}

			PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl
					.getValue("propertyDescriptors.{? name == '"
							+ propertyDescriptor.getName() + "'}[0]",
							Introspector.getBeanInfo(clonedDescriptor
									.getBeanType()));

			Method readMethod = beanPropDescriptor.getReadMethod();

			if (couldAddProperty(annotations, readMethod.getAnnotations())) {
				properties.add(propertyDescriptor);
			}

		} catch (Exception ex) {
			// don't care
		}
	}

	boolean couldAddProperty(List<Class> expectedAnnotations,
			Annotation[] actualAnnotations) {

		if (expectedAnnotations.size() == 0)
			return true;

		for (Annotation annotation : actualAnnotations) {
			if (expectedAnnotations.contains(annotation.annotationType()))
				return true;
		}

		return false;

	}

	/**
	 * Per aggirare lo strano comportamento di freemarker per cui non funzionava
	 * correttamente dentro web-integration.jar. Da indagare...
	 * 
	 * @author nicola
	 * 
	 */
	@SuppressWarnings("unchecked")
	private class SameName implements TemplateMethodModel {
		public Object exec(List args) throws TemplateModelException {
			if (args.size() != 1 || ((String) args.get(0)).trim().length() == 0) {
				throw new TemplateModelException(
						"You must specify the field name");
			}
			String name = (String) args.get(0);
			return name;
		}
	}

	@SuppressWarnings("unchecked")
	private class FieldNameConverter implements TemplateMethodModel {
		public Object exec(List args) throws TemplateModelException {
			if (args.size() != 1 || ((String) args.get(0)).trim().length() == 0) {
				throw new TemplateModelException(
						"You must specify the field name");
			}
			String name = (String) args.get(0);
			if (Character.isLowerCase(name.charAt(0)))
				return Utils.upperFirstLetter(name);
			else
				return Utils.lowerFirstLetter(name);
		}
	}

	@SuppressWarnings("unchecked")
	private class NameGuesser implements TemplateMethodModel {
		public Object exec(List args) throws TemplateModelException {
			if (args.size() != 1 || ((String) args.get(0)).trim().length() == 0) {
				throw new TemplateModelException(
						"You must specify the field name");
			}
			String name = (String) args.get(0);

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < name.length(); i++) {
				char c = name.charAt(i);
				if (i == 0) {
					sb.append(Character.toUpperCase(c));
				} else if (Character.isUpperCase(c)) {
					sb.append(" ");
					sb.append(c);
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		}
	}

	public static String camelNotation(String name) {
		if (Character.isLowerCase(name.charAt(0)))
			return Utils.upperFirstLetter(name);
		else
			return Utils.lowerFirstLetter(name);
	}

	private String cat(String a, String b) {
		return parameters.get(a) + "." + parameters.get(b);
	}

	private String getPackageName(IClassDescriptor descriptor) {
		String type = descriptor.getType().getName();
		String pack = type.substring(0, type.indexOf(".model."));
		return pack;
	}

	/**
	 * @return the parameters
	 * @uml.property name="parameters"
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}
}
