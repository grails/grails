<g:set var="updateElement" value="${update ?: 'contentPane'}"/>

<g:render template="viewActions" model="[content:content, update:updateElement]" />
<g:render template="/common/messages" model="${pageScope.getVariables() + [bean:content]}" />

<wiki:text key="${content?.title}">
    ${content?.body}
</wiki:text>

<g:javascript>
   if($('message')!=null) {
        Effect.Fade('message', {delay:3})
   }
</g:javascript>