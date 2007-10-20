	<managed-bean>
		<managed-bean-name>${mbean_form_name}</managed-bean-name>
		<managed-bean-class>
			it.kion.plitvice.webapp.action.VerbaleForm
		</managed-bean-class>
		<managed-bean-scope>request</managed-bean-scope>
		<managed-property>
			<property-name>id</property-name>
			<value>${r"#{"}param.id}</value>
		</managed-property>
		<managed-property>
			<property-name>serviceManager</property-name>
			<value>#{verbaleManager}</value>
		</managed-property>
		<managed-property>
			<property-name>autoComplete</property-name>
		    <map-entries>
		       <map-entry>
		         <key>materia</key>
		         <value>#{autoCompleteMateriaBean}</value>
		       </map-entry>
		    </map-entries>
		</managed-property>
	</managed-bean>

</faces-config>

 