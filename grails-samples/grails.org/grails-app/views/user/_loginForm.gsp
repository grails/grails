<g:set var="updateElement" value="${update ?: 'contentPane'}"/>

<g:render template="/common/messages" model="${pageScope.getVariables() + [bean:wikiPage]}" />

<h1>Site Login</h1>
<div id="loginForm" class="userForm">
    <g:set var="formBody">
        <div  class="inputForm">
            <p>
                 <span class="label"><label for="login">Username:</label></span> <g:textField class="textInput" name="login" value="${formData?.login}"/>
             </p>
             <p>
                 <span class="label"><label for="password">Password:</label></span> <g:field type="password" name="password" />
             </p>

        </div>
        <g:hiddenField name="originalURI" value="${originalURI ?: formData?.originalURI}" />
        <g:each in="${formData}" var="d">
           	<g:if test="${!(d.value instanceof Map) && d.key != 'login'}">
                <g:hiddenField name="${d.key}" value="${d.value}" />
            </g:if>
        </g:each>

		<g:hiddenField name="async" value="${async ?: false}"></g:hiddenField>
        <div class="formButtons">
            <g:submitButton name="Submit" value="Login" />
        </div>
    </g:set>

    <g:if test="${true == async}">
        <g:formRemote name="login" url="[controller:'user',action:'login']" update="${updateElement}">
            ${formBody}
        </g:formRemote>
    </g:if>
    <g:else>
        <g:form name="login" controller='user' action="login">
            ${formBody}
        </g:form>
    </g:else>

     <div>

         <g:set var="registerLink">
            <g:if test="${true == async}"><a href="#" onclick="new Ajax.Updater('${updateElement}','${createLink(controller:'user', action:'register')}',{method:'GET',asynchronous:true,evalScripts:true,parameters:Form.serialize('login')});return false;">click here</a></g:if>
            <g:else><g:link controller="user" action="register">click here</g:link></g:else>
         </g:set>
         If you do not have an account ${registerLink} to register. If you have forgotten your password visit the <g:link controller="user" action="passwordReminder">password</g:link> reminder page.
     </div>
</div>

%{--<g:render template="/common/messages_effects" model="${pageScope.getVariables()}" />--}%
