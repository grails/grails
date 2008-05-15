<g:render template="/common/messages" model="${pageScope.getVariables() + [bean:wikiPage]}" />

<h1>Site Login</h1>
<div id="loginForm" class="userForm">
    <g:set var="formBody">
        <div  class="inputForm">
            <p>
                 <span class="label"><label for="login">Username:</label></span> <g:textField class="textInput" name="login" />
             </p>
             <p>
                 <span class="label"><label for="password">Password:</label></span> <g:field type="password" name="password" />
             </p>

        </div>
        <g:hiddenField name="originalURI" value="${originalURI ?: params.originalURI}" />
        <g:each in="${formData?}" var="d">
            <g:if test="${d.key != 'login' && d.key != 'password'}">
                <g:hiddenField name="${d.key}" value="${d.value}" />
            </g:if>
        </g:each>


        <div class="formButtons">
            <g:submitButton name="Submit" value="Login" />
        </div>
    </g:set>

    <g:if test="${true == async}">
        <g:formRemote name="login" url="[controller:'user',action:'login']" update="contentPane">

            ${formBody}
        </g:formRemote>
    </g:if>
    <g:else>
        <g:form name="login" action="login">
            ${formBody}
        </g:form>
    </g:else>

     <div>

         If you do not have an account <a href="#" onclick="new Ajax.Updater('contentPane','${createLink(controller:'user', action:'register')}',{method:'GET',asynchronous:true,evalScripts:true,parameters:Form.serialize('login')});return false;">click here</a>  to register.
     </div>
</div>

<g:render template="/common/messages_effects" model="${pageScope.getVariables()}" />