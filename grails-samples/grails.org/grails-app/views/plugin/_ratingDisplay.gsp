<table class="ratingDisplay">
    <tr>
        <g:each var="i" in="${(1..5)}">
            <g:if test="${average >= i}">
                <td><div class="star on"><a></a></div></td>
            </g:if>
            <g:else>
                <g:set var="starWidth" value="${100 * (average - (i-1))}"/>
                <g:if test="${starWidth < 0}"><g:set var="starWidth" value="0"/></g:if>
                <td><div class="star on"><a style="width:${starWidth}%"></a></div></td>
            </g:else>
        </g:each>
        <td>(${votes ?: 0})</td>
    </tr>
</table>