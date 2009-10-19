<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="regola" uri="http://org.regola-kit/mvc"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<span class="titoloPagina"><fmt:message key="${field(model_name)}.title" /></span>
<br>
<br>
<span class="sottotitoloPagina">
	<fmt:message key="${field(model_name)}.subtitle" />
</span>
<br>
<br>

<form:form modelAttribute="pattern" method="post" action="${r"${"}flowExecutionUrl}">

<table summary="Tabella di struttura" class="tb0">
	<tbody>
		<tr>
			<td colspan="2">
			<table summary="filtri" class="toptabs">
			 	<tbody><tr><th class="selectedtab">Filtri ricerca</th></tr>
			</tbody></table>   					
			<table summary="filtri" class="toptools">
				<tbody>
					<tr>
						<td class="toolcontentt" colspan="2">
						<table class="tbFiltri">
							<tbody>
								<tr>
									<td class="filtro2" style="vertical-align: middle;">
								    	<label><fmt:message  key="${r"${"}prop.id}" /></label>
									</td>								    	
								    <td class="filtro2" style="vertical-align: middle;">								    	
								    	<form:input path="id"/>
									</td>
									<td class="bottonecerca" rowspan="2">
										<input type="submit" id="filtra" class="button" 
											name="_eventId_search${model_name}" value="<fmt:message  key="button.search" />" />
									</td>
								</tr>
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
	</tbody>
</table>

<table class="iceDataTblOutline" style="border-collapse: collapse; border-spacing: 0px; width: 95%">

	<tr>
		<td colspan="${r"${"}fn:length(pattern.visibleProperties)+1}" style="background-color:#EEEEEE; padding: 2px;">
			<input type="submit" name="_eventId_new" value="<fmt:message  key="button.new" />" class="button" />			
		</td>
	</tr>	

	<tr class="header">
		<c:forEach items="${r"${"}pattern.visibleProperties}" var="prop">
			<th class="iceTblHeader header1">
				<span class="iceOutputText"><fmt:message  key="${r"${"}prop.label}" /></span>
			</th>
		</c:forEach>
		<th class="iceTblHeader header1"/>
	</tr>
	<c:if test="${r"${empty list}"}">
		<tr class="rigaPari">
			<td colspan="${r"${"}fn:length(pattern.visibleProperties)+1}">Nessun risultato disponibile</td>
		</tr>
	</c:if>	
	<c:forEach items="${r"${"}list}" var="row" varStatus="status">
		<tr class="${r"${"}status.index%2==0 ? 'rigaPari' : 'rigaDispari'}">
			<c:forEach items="${r"${"}pattern.visibleProperties}" var="prop">
				<td class="colonna">
						<p style="padding:0;border:0;margin:0">
						${r"${"}regola:value(prop,row)} &nbsp;
						</p>
				</td>
			</c:forEach>
			<td class="colonna">
				<a href="${r"${"}flowExecutionUrl}&_eventId=edit&idx=${r"${"}status.index}" class="button"><fmt:message  key="button.edit" /></a>
				<a href="${r"${"}flowExecutionUrl}&_eventId=cancel${model_name}&idx=${r"${"}status.index}" class="button"
					onclick="return confirm('Confermi la cancellazione?');"><fmt:message  key="button.delete" /></a>
			</td>
		</tr>
	</c:forEach>
</table>

<table >
	<tr class='rigaPari'>
		<!-- e.g. [|<] [<] [>] [>|] (i bottoni di scorrimento) -->
		<td><a href="${r"${"}flowExecutionUrl}&_eventId=moveFirst"><img  src="https://starc.unibo.it/images/arrow-first.gif" /></a></td> 
		<td><a href="${r"${"}flowExecutionUrl}&_eventId=movePrevious" ><img  src="https://starc.unibo.it/images/arrow-previous.gif" /></a></td>		
		<td><a href="${r"${"}flowExecutionUrl}&_eventId=moveNext" ><img  src="https://starc.unibo.it/images/arrow-next.gif" /></a></td>
		<td><a href="${r"${"}flowExecutionUrl}&_eventId=moveLast" ><img  src="https://starc.unibo.it/images/arrow-last.gif" /></a></td>

		<!-- e.g. Pagina 1 di 2 -->
		<td class="iceOutputText">Risultati da ${r"${"}pattern.firstElementNumber} a ${r"${"}pattern.lastElementNumber} di ${r"${"}pattern.totalItems}, Pagina ${r"${"}pattern.currentPage + 1}/${r"${"}pattern.lastPage+1}</td> 
		
		<td class="iceOutputText"><fmt:message key="paginator.pageSize" /></td>

		<td>
			<form:select  id="pageSize" path="pageSize" cssClass="paginatore">
					<form:option label="10" value="10"/>
					<form:option label="20" value="20"/>
					<form:option label="30" value="30"/>
					<form:option label="40" value="40"/>
					<form:option label="50" value="50"/>
			</form:select>
			<input type="image" name="_eventId_pageSize" title="Imposta dimensione della pagina" src="https://starc.unibo.it/images/arrow-reload.gif" />
		</td>

	</tr>			
</table>
</form:form>