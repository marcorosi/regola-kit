
	<navigation-rule>
		<from-view-id>/${mbean_list_page}</from-view-id>
		<navigation-case>
			<from-outcome>edit</from-outcome>
			<to-view-id>/${mbean_form_page}</to-view-id>
		</navigation-case>
	</navigation-rule>

	<navigation-rule>
		<from-view-id>/${mbean_form_page}</from-view-id>
		<navigation-case>
			<from-outcome>save</from-outcome>
			<to-view-id>/${mbean_list_page}</to-view-id>
		</navigation-case>
		<navigation-case>
			<from-outcome>cancel</from-outcome>
			<to-view-id>/${mbean_list_page}</to-view-id>	
		</navigation-case>
	</navigation-rule>
    
</faces-config>


 