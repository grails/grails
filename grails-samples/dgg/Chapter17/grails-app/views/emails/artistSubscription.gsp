<%@ page contentType="text/plain"%>
Dear ${user.firstName} ${user.lastName},

One of your favorite artists ${artist.name} has released a new album called ${album.title}!

It is available now on gTunes at <g:createLink controller="album" action="display" id="${album.id}" absolute="true" />

Kind Regards,

The gTunes Team