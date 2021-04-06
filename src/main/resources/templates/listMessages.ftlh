<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>


<@l.logout></@l.logout>

<form method="get", action="/all">
    <input type="text" name="filter", value="${filter}">
    <input type="submit">
</form>

    <form action="/add" method="post">
        <input type="text" name="text">
        <input type="text" name="tag">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="submit">
    </form>
<h1>Product list</h1>
<#list messages as messege>

    <div>
        <b>${messege.id}</b>
        <span>${messege.text}</span>
        <i>${messege.tag}</i>
        <strong>${messege.getAuthorName()}</strong>
    </div>
    <#else>
    No massages
</#list>
</@c.page>