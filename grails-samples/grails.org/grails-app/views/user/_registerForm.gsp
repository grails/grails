
<g:render template="/common/messages" model="${pageScope.getVariables() + [bean:user]}" />

<h1>Site Registration</h1>
<div id="registerForm" class="userForm">

    <g:formRemote name="login" url="[controller:'user',action:'register']" update="contentPane">
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
                <span class="label"><label for="email">Content Alerts?:</label></span> <g:checkBox name="info.emailSubscribed" value="${false}"/>
           </p>

        </div>


        <g:hiddenField name="originalURI" value="${originalURI}" />
        <g:each in="${formData?}" var="d">
            <g:if test="${!['login','password', 'password2', 'originalURI', 'email'].contains(d.key)}">
                <g:hiddenField name="${d.key}" value="${d.value}" />
            </g:if>
        </g:each>

       <div class="formButtons">
            <g:submitButton name="Submit" value="Register" />
        <div class="formButtons">
    </g:formRemote>

</div>

<g:render template="/common/messages_effects" model="${pageScope.getVariables()}" />