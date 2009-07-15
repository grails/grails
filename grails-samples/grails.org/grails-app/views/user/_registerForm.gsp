
<g:render template="/common/messages" model="${pageScope.getVariables() + [bean:user]}" />



<h1>Site Registration</h1>
<div id="registerForm" class="userForm">
    <g:set var="formBody">
       <div  class="inputForm">


           <p>
                <span class="label"><label for="login">Username:</label></span> <g:textField name="login" value="${params.login}" />
            </p>
            <p>
                <span class="label"><label for="password">Password:</label></span> <g:field type="password" name="password" value="${params.password}" />
            </p>
            <p>
                <span class="label"><label for="password">Confirm Password:</label></span> <g:field type="password" name="password2" value="${params.password2}" />
            </p>
            <p>
                 <span class="label"><label for="email">Email:</label></span> <g:textField name="email" value="${params.email}"/>
            </p>
           <p>
                <span class="label"><label for="email">Receive E-mail Updates for Content Changes?:</label></span> <g:checkBox name="emailSubscribed" value="${false}"/>
           </p>

        </div>


        <g:hiddenField name="originalURI" value="${originalURI}" />
        <g:each in="${formData?}" var="d">
            <g:if test="${!['login','password', 'password2', 'originalURI', 'email'].contains(d.key)}">
                <g:hiddenField name="${d.key}" value="${d.value}" />
            </g:if>
        </g:each>

       <div class="formButtons" style="margin-top:100px;">
            <g:submitButton name="Submit" value="Register" />
        </div>
    </g:set>


    <g:if test="${true == async}">
        <g:formRemote name="register" url="[controller:'user',action:'register']" update="contentPane">
              ${formBody}
        </g:formRemote>
    </g:if>
    <g:else>
        <g:form name="register" url="[controller:'user',action:'register']" update="contentPane">
              ${formBody}
        </g:form>
    </g:else>



</div>

<g:render template="/common/messages_effects" model="${pageScope.getVariables()}" />