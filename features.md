# Features
*   Hexagonal architecture
	*   Design Patterns
		*   Domain Driven Design (package domain)
		*   Model View Controller (package adapter)
		*   Factory Pattern for different kinds of repositories
		*   Emphasis on decoupling, separation of concerns, SOLID principles
		*   Based in the [Clean Architecture whitepaper](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)
*   Framework (package core)
	*   Request handler with URI routing
	*   Session support with cookie management
	*   Database abstraction layer (package core.database)
		*   Relational SQL based data source implemented
		*   Support for **SQLite**
			*   File based SQLite support
			*   In memory SQLite support
		*   Support for **MySQL**
		*   Support for **MongoDB** (NoSQL)
		*   Architecture is prepared to add new data sources, relational or NoSQL
		*   Retrieve configuration from environment vars
	*   Templating engine abstraction layer (package core.templating)
		*   Basic native template parser
		*   Support for **JTwig** templating engine
		*   Architecture is prepared to add new templating engines
*   Adapter (package adapter)
	*   Application controller
		*   Application is able to create new sessions through user login
		*   Logged users will go to welcome page by default
			*   Welcome page exposes roles and allowed page links for the logged user, features a logout button
		*   Application records last page access attempt when user is not logged in
			*   Login will redirect to recorded page instead of welcome page
			*   If the user does not have the role to access the recorded page or the page does not exist, will go to welcome page
		*   Application has restricted access pages
			*   Access is possible to the users attaining the proper roles
			*   Pages display welcome message with user name, back to welcome page button, and logout button
		*   Application has a number of HTTP error pages
			*   HTTP 403 forbidden page when user tries to access page without clearance
			*   HTTP 404 not found page when user tries to access inexistent page
			*   HTTP 500 internal page if unhandled exceptions emerge
	*   REST API controller
		*   Basic authentication
			*   REST API is stateless, handles Basic Authentication per request through the Authorization header
		*   Content negotiation
			*   REST API can serve resources in JSON or XML depending on Accept header
			*   **Gson** and **JAXB** libraries used for response formatting
			*   Request body supports the JSON format only
		*   REST API exposes users collection (endpoint api/users)
			*   GET will retrieve list of users and their roles
			*   POST will create a new user resource
		*   REST API exposes user resource (endpoint api/users/<id>)
			*   GET will retrieve the user and his/her roles
			*   PUT will update the user resource with supplied data
			*   DELETE will remove the user resource permanently
	*   Repositories
		*   There are two types of repos, UserRepository and RoleRepository, each one has an interface in the domain model
		*   For each repo type, there can be a number of implementing classes subject to underlying data source technology
			*   Under the package adapter.repository.relational, repos of type Role and User have been implemented, targeting a relational database with SQL
		*   Other kinds of repos could be developed for document based databases like MongoDB or ElasticSearch
		*   Another model could use LDAP and so on...
*   Domain (package domain)
	*   Code under this layer is immutable, changes in underlying system, framework, data source, viewport etc.. will not affect it
	*   Other layers can work with the objects of this layer, a good example are the controllers, that work with the use cases, and the repositories, that work with the entities
	*   On the contrary, objects of this layer are agnostic of what lies in the bottom, access to adapter/core packages is not permitted from this context
	*   The domain layer features the following
		*   A helper class
			*   Objects of this layer cannot access the Core package so they need to have their own helper
		*   The domain.contract package
			*   This package contains interfaces that serve as an abstraction for decoupling the domain package from the supporting architecture
		*   Entities
			*   They represent the resources that the application works with, they can hold enterprise-level business logic
		*   Service
		    *   The user service implements some logic that are common to many usecases or is not suitable to be implemented in a usecase or entity
		*   A number of usecase classes
			*   Each usecase represents one type of interaction between the application and the user
			*   Each usecase leverages a number of different outputs as a result of their execution
			*   The business rules of the platform, including the web application and the REST API, lies within these usecases
			*   There are some usecases for the web application, and others for the REST API
			*   Yet there is a usecase that applies to both, which is the authentication usecase
*   Unit tests
	*   Unit tests are currently covering only the usecase classes, inside of the domain package
	*   Each usecase class, build with **JUnit**, has a test class that covers all possible flows
	*   Usecases and services are designed to support dependency injection
    *   For the sake of testing, dependencies are mocked with the **Mockito** library