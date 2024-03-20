# SS3-SpringSecurityV3Starter
A starter kit for a Spring Security full stack app using Spring Boot v3

### Current workflow
>**/register** endpoint
To create a new *regular* user, go through the registration form. After a successful registration submission, the app should redirect to the log in page.

>**/signin** endpoint
Login form by using the credentials provided during registration. If successful login, there should be 3 tokens created and stored as **cookies** in the browser (accessToken, refreshToken, JSESSIONID).
>>>**JSESSIONID** is for session management and automatically created by Tomcat Servlet.

>**/success** endpoint
After successful login, the app should redirect the user to this endpoint with a logout button.
>>>The **logout** button should clear the user's browser of all 3 cookies after successful logout.

>**/products** endpoint
The products endpoint is *only* accessible after **authentication**. If an user tries to access this endpoint without prior authentication, the app should redirect the user to the log in page.

>**/admin/users** endpoint
Similarly to the products endpoint, the users endpoint if *only* accessible for **ADMIN** role users. If an user without an ADMIN role tries to access this endpoint, the app should redirect to the error page. 
>>>To change users, please visit the success endpoint.

>**/admin/dashboard** endpoint
The dashboard should list all users and provide an **Elevate to Admin** button on the same row. There should be a logout button on the table. After elevating a new user to ADMIN role, the user should be able to access the users endpoint.