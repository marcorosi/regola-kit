# ${field(model_name)}

${field(model_name)}.title=${field(model_name)}
${field(model_name)}.subtitle=${field(model_name)}
${field(model_name)}.bartitle=${field(model_name)}
<#list idProperties as proprieta >
${field(model_name)}.column.id.${same(proprieta.name)}=${guessName(proprieta.name)}
</#list>
<#if idProperties?size == 0 >
${field(model_name)}.column.id=id
 </#if>
<#list modelProperties as proprieta >
${field(model_name)}.column.${same(proprieta.name)}=${guessName(proprieta.name)}
</#list>

