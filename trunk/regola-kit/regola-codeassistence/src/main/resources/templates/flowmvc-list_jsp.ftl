<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="regola" uri="http://org.regola-kit/mvc"%>
<table class="iceDataTblOutline"
	Style="border-collapse: collapse; border-spacing: 0px;">
	<tr>
		<c:forEach items="${r"${"}pattern.visibleProperties}" var="prop">
			<th class="iceTblHeader header1">
				<span class="iceOutputText">
					<fmt:message  key="${r"${"}prop.label}" /> 
				</span>
			</th>
		</c:forEach>
		<th  />
	</tr>
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
				<a href="${r"${"}flowExecutionUrl}&_eventId=edit&idx=${r"${"}status.index}" class="buttonNew">Edita</a>
			</td>
			<td class="colonna">
				<a href="${r"${"}flowExecutionUrl}&_eventId=cancel${model_name}&idx=${r"${"}status.index}" class="buttonNew">Cancella</a>
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
		<td class="iceOutputText">Pagina ${r"${"}pattern.currentPage + 1} di ${r"${"}pattern.lastPage+1}</td> 
		
		<!-- e.g. Righe per pagina [20] -->
		<td>
		<form:form modelAttribute="pattern" method="post" action="${r"${"}flowExecutionUrl}">
			<fmt:message key="paginator.pageSize" />
			<form:select  id="pageSize" path="pageSize">
					<form:option label="10" value="10"/>
					<form:option label="20" value="20"/>
					<form:option label="30" value="30"/>
					<form:option label="40" value="40"/>
					<form:option label="50" value="50"/>
			</form:select>
			<input class="buttonNew" type="submit" name="_eventId_pageSize" value="Modifica" />
			<input type="hidden" name="idx" value="1" />
			<input class="buttonNew" type="submit" name="_eventId_edit" value="Edita" />
		</form:form></td>
	</tr>			
</table>
 