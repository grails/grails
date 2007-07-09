                                                                        
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
         <title>Book List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
        </div>
        <div class="body">
           <h1>Book List</h1>
            <g:if test="${flash.message}">
                 <div class="message">
                       ${flash.message}
                 </div>
            </g:if>
           <table>
             <thead>
               <tr>
               
                   	    <g:sortableColumn property="id" title="Id" />
                  
                   	    <g:sortableColumn property="title" title="Title" />

                   	    <g:sortableColumn property="author" title="Author" />

                   	    <g:sortableColumn property="price" title="Price" />
                  
                        <th></th>
               </tr>
             </thead>
             <tbody>
               <g:each in="${bookList}">
                    <tr>
                       
                            <td>${it.id?.encodeAsHTML()}</td>
                       
                            <td>${it.title?.encodeAsHTML()}</td>

                            <td>${it.author?.encodeAsHTML()}</td>

                            <td>${it.price?.encodeAsHTML()}</td>
                       
                       <td class="actionButtons">
                            <span class="actionButton"><g:link action="shoppingCart" id="${it.id}" event="chooseBook">Buy</g:link></span>
                       </td>
                    </tr>
               </g:each>
             </tbody>
           </table>
               <div class="paginateButtons">
                   <g:paginate total="${Book.count()}" />
               </div>
        </div>
    </body>
</html>
