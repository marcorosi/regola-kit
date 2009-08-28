<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form:form modelAttribute="${field(model_name)}" action="${r"${"}flowExecutionUrl}">
	<table>
		<tbody>

          
          <#list modelProperties as proprieta >
			
					<!-- ${same(proprieta.name)} -->
					<tr>
					<td><fmt:message  key="${field(model_name)}.column.${same(proprieta.name)}" /></td>
					<td><form:input path="${same(proprieta.name)}"/></td>
					<td></td>
					</tr>
		  </#list>
			<tr>
				<td></td>
				<td><input class="buttonNew" type="submit" name="_eventId_cancel${model_name}" value="Cancel"/>
					<input class="buttonNew" type="submit" name="_eventId_save${model_name}" value="Save"/></td>
				<td></td>
			</tr>
		</tbody>
	</table>
	
	
</form:form>