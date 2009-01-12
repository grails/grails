<%@ page contentType="text/plain"%>
Dear ${user.firstName} ${user.lastName},

Congratulations! You have registered with gTunes, giving you access to a huge
collection of music. 

Your login id is: ${user.login}

You can use the following link to login: <g:createLink controller="store" absolute="true" />

Kind Regards,

The gTunes Team