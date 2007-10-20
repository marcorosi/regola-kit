    
    <bean id="${service_bean_name}" class="${service_impl_package}.${service_impl_name}">      
	    <constructor-arg>          
	        <bean class="it.kion.regola.dao.hibernate.GenericDaoHibernate" autowire="byType">              
	            <constructor-arg value="${model_class}"/>          
	        </bean>      
    </constructor-arg>  
   </bean>
    
</beans>    