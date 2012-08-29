Elenco delle variabili disponibili nei template di Regola kit:

allProperties : 
  <#list allProperties as proprieta >
	   proprieta.name : ${proprieta.name}
  </#list>
collectionsProps :
  <#list collectionsProps as proprieta >
	   proprieta.name : ${proprieta.name}
  </#list>
dao_bean_name : ${dao_bean_name}
dao_impl_class : ${dao_impl_class}
dao_impl_name : ${dao_impl_name}
dao_impl_package : ${dao_impl_package}
dao_interface_class : ${dao_interface_class}
dao_interface_name : ${dao_interface_name}
dao_package : ${dao_package}
field : funzione
filter_class : ${filter_class}
filter_name : ${filter_name}
guessName : funzione
idProperties : 
  <#list idProperties as proprieta >
	   proprieta.name : ${proprieta.name}
  </#list> 
id_class : ${id_class}
id_name : ${id_name}
mbean_form_name : ${mbean_form_name}
mbean_form_page : ${mbean_form_page}
mbean_form_page_url : ${mbean_form_page_url}
mbean_list_name : ${mbean_list_name}
mbean_list_page : ${mbean_list_page}
mbean_list_page_url : ${mbean_list_page_url}
mbean_package : ${mbean_package}
mock_name : ${mock_name}
modelProperties :
  <#list modelProperties as proprieta >
	   proprieta.name : ${proprieta.name}
  </#list> 
model_class : ${model_class}
model_name : ${model_name}
notCollectionsProps : 
  <#list notCollectionsProps as proprieta >
	   proprieta.name : ${proprieta.name}
  </#list>
package : ${package}
pattern_package : ${pattern_package}
same : funzione
service_bean_name : ${service_bean_name}
service_impl_class : ${service_impl_class}
service_impl_name : ${service_impl_name}
service_impl_package : ${service_impl_package}
service_interface_class : ${service_interface_class}
service_interface_name : ${service_interface_name}
service_package : ${service_package}
tipo : ${tipo}
tipoId : ${tipoId}