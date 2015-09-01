#  ##############  #
#  Install Addons  #
#  ##############  #
#  If the following plugins are not installed
#  addon-install-from-git --url https://github.com/forge/addon-arquillian.git --coordinate org.arquillian.forge:arquillian-addon



#  #####################  #
#  Creates a new project  #
#  #####################  #

project-new --named agoncal-application-cdbookstore --topLevelPackage org.agoncal.application.cdbookstore --finalName applicationCDBookStore ;


#  Setup the deployment descriptors to Java EE 7
#  ############
jpa-setup --persistenceUnitName applicationCDBookStorePU --jpaVersion 2.1 ;
cdi-setup --cdiVersion 1.1 ;
ejb-setup --ejbVersion 3.2 ;
faces-setup --facesVersion 2.2 ;
servlet-setup --servletVersion 3.1 ;
rest-setup --jaxrsVersion 2.0 ;

#  Setup Arquillian
#  ############
arquillian-setup --arquillianVersion 1.1.8.Final --testFramework junit --testFrameworkVersion 4.12 --containerAdapter wildfly-remote --containerAdapterVersion 8.2.1.Final ;



#  ########################  #
#  Creates the domain model  #
#  ########################  #


#  Constraints
#  ############
constraint-new-annotation --named Email ;


#  Address embeddable
#  ############
jpa-new-embeddable --named Address ;
jpa-new-field --named street1 --length 50 --not-nullable ;
jpa-new-field --named street2 ;
jpa-new-field --named city  --length 50 --not-nullable ;
jpa-new-field --named state ;
jpa-new-field --named zipcode --columnName zip_code --length 10 --not-nullable ;
jpa-new-field --named country --not-nullable ;
# Constraints
constraint-add --onProperty street1 --constraint Size --min 5 --max 50 ;
constraint-add --onProperty street1 --constraint NotNull ;
constraint-add --onProperty city --constraint Size --min 2 --max 50 ;
constraint-add --onProperty city --constraint NotNull ;
constraint-add --onProperty zipcode --constraint Size --min 1 --max 10 ;
constraint-add --onProperty zipcode --constraint NotNull ;
constraint-add --onProperty country --constraint NotNull ;


#  CreditCardType enumeration
#  ############
java-new-enum --named CreditCardType --targetPackage ~.model ;
java-new-enum-const VISA ;
java-new-enum-const MASTER_CARD ;
java-new-enum-const AMERICAN_EXPRESS ;

java-new-class --named CreditCardConverter --targetPackage ~.model ;


# CreditCard embeddable
# ############
jpa-new-embeddable --named CreditCard ;
jpa-new-field --named creditCardNumber --columnName credit_card_number --length 30 --not-nullable ;
jpa-new-field --named creditCardType --type ~.model.CreditCardType --columnName credit_card_type ;
jpa-new-field --named creditCardExpDate --columnName credit_card_expiry_date --length 5 --not-nullable ;
# Constraints
constraint-add --onProperty creditCardNumber --constraint NotNull ;
constraint-add --onProperty creditCardNumber --constraint Size --min 1 --max 30 ;
constraint-add --onProperty creditCardType --constraint NotNull ;
constraint-add --onProperty creditCardExpDate --constraint NotNull ;
constraint-add --onProperty creditCardExpDate --constraint Size --min 1 --max 5 ;


#  User role enumeration
#  ############
java-new-enum --named UserRole --targetPackage ~.model
java-new-enum-const USER ;
java-new-enum-const ADMIN ;


#  User entity
#  ############
jpa-new-entity --named User --tableName t_user ;
jpa-new-named-query --named User.findByEmail --query 'SELECT u FROM User u WHERE u.email = :email' ;
jpa-new-named-query --named User.findByUUID --query 'SELECT u FROM User u WHERE u.uuid = :uuid' ;
jpa-new-named-query --named User.findByLogin --query 'SELECT u FROM User u WHERE u.login = :login' ;
jpa-new-named-query --named User.findByLoginPassword --query 'SELECT u FROM User u WHERE u.login = :login AND u.password = :password' ;
jpa-new-named-query --named User.findAll --query 'SELECT u FROM User u' ;
jpa-new-field --named firstName --length 50 --columnName first_name --not-nullable ;
jpa-new-field --named lastName --length 50 --columnName last_name --not-nullable ;
jpa-new-field --named telephone ;
jpa-new-field --named email ;
jpa-new-field --named login --length 10 --not-nullable ;
jpa-new-field --named password --length 256 --not-nullable ;
jpa-new-field --named uuid --length 256 ;
# TODO FORGE-2464
# jpa-new-field --named UserRole --type ~.model.UserRole --columnName user_role
jpa-new-field --named UserRole --type org.agoncal.application.cdbookstore.model.UserRole --columnName user_role ;
jpa-new-field --named dateOfBirth --type java.util.Date --temporalType DATE --columnName date_of_birth ;
# Constraints
constraint-add --onProperty login --constraint NotNull ;
constraint-add --onProperty login --constraint Size --min 1 --max 10 ;
constraint-add --onProperty email --constraint NotNull ;
constraint-add --onProperty password --constraint NotNull ;
constraint-add --onProperty password --constraint Size --min 1 --max 256 ;
constraint-add --onProperty uuid --constraint Size --min 1 --max 256 ;
constraint-add --onProperty firstName --constraint NotNull ;
constraint-add --onProperty firstName --constraint Size --min 2 --max 50 ;
constraint-add --onProperty lastName --constraint NotNull ;
constraint-add --onProperty lastName --constraint Size --min 2 --max 50 ;
constraint-add --onProperty dateOfBirth --constraint Past ;


#  Language enum
#  ############
java-new-enum --named Language --targetPackage ~.model ;
java-new-enum-const ENGLISH ;
java-new-enum-const FRENCH ;
java-new-enum-const SPANISH ;
java-new-enum-const PORTUGUESE ;
java-new-enum-const ITALIAN ;
java-new-enum-const FINISH ;
java-new-enum-const GERMAN ;
java-new-enum-const DEUTSCH ;
java-new-enum-const RUSSIAN ;

#  Artist Mapped Superclass
#  ############
jpa-new-mapped-superclass --named Artist ;
jpa-new-field --named firstName --length 50 ;
jpa-new-field --named lastName --length 50 ;
jpa-new-field --named bio --length 5000 ;
jpa-new-field --named dateOfBirth --type java.util.Date --temporalType DATE ;
jpa-new-field --named age --type java.lang.Integer ;

constraint-add --onProperty firstName --constraint NotNull ;
constraint-add --onProperty firstName --constraint Size --min 2 --max 50 ;
constraint-add --onProperty lastName --constraint NotNull ;
constraint-add --onProperty lastName --constraint Size --min 2 --max 50 ;
constraint-add --onProperty bio --constraint Size --max 5000 ;
constraint-add --onProperty dateOfBirth --constraint Past ;


#  Author Entity
#  ############
jpa-new-entity --named Author ;
jpa-new-field --named preferredLanguage --type ~.model.Language ;

#  Musician Entity
#  ############
jpa-new-entity --named Musician ;
jpa-new-field --named preferredInstrument ;

#  Publisher Entity
#  ############
jpa-new-entity --named Publisher ;
jpa-new-field --named name --length 30 ;

constraint-add --onProperty name --constraint NotNull ;
constraint-add --onProperty name --constraint Size --max 30 ;

#  Item Mapped Superclass
#  ############
jpa-new-mapped-superclass --named Item ;
jpa-new-field --named title --length 30 ;
jpa-new-field --named description --length 3000 ;
jpa-new-field --named unitCost --type java.lang.Float ;

constraint-add --onProperty title --constraint NotNull ;
constraint-add --onProperty title --constraint Size --min 1 --max 30 ;
constraint-add --onProperty description --constraint Size --min 1 --max 3000 ;
constraint-add --onProperty unitCost --constraint Min --value 1 ;


#  Category entity
#  ############
jpa-new-entity --named Category ;
jpa-new-field --named name --length 100 ;

constraint-add --onProperty name --constraint NotNull ;
constraint-add --onProperty name --constraint Size --max 100 ;


#  Book Entity
#  ############
jpa-new-entity --named Book ;
jpa-new-field --named isbn --length 15 ;
jpa-new-field --named nbOfPage --type java.lang.Integer ;
jpa-new-field --named publicationDate --type java.util.Date --temporalType DATE ;
jpa-new-field --named language --type ~.model.Language ;
# Relationships
jpa-new-field --named category --type ~.model.Category --relationshipType Many-to-One
jpa-new-field --named authors --type ~.model.Author --relationshipType One-to-Many ;
jpa-new-field --named publisher --type ~.model.Publisher --relationshipType Many-to-One ;

constraint-add --onProperty isbn --constraint NotNull ;
constraint-add --onProperty isbn --constraint Size --max 15 ;
constraint-add --onProperty nbOfPage --constraint Min --value 1 ;


#  CD Entity
#  ############
jpa-new-entity --named CD ;
jpa-new-field --named totalDuration --type java.lang.Float ;
jpa-new-field --named musicCompany ;
jpa-new-field --named genre ;
jpa-new-field --named musicians --type ~.model.Musician --relationshipType Many-to-Many ;

#  Musician Entity
#  ############
cd ../Musician.java ;
jpa-new-field --named cds --type ~.model.CD --relationshipType Many-to-Many ;


#  OrderLine entity
#  ############
jpa-new-entity --named OrderLine --tableName order_line ;
jpa-new-field --named quantity --type java.lang.Integer --not-nullable;
# Relationships
jpa-new-field --named item --type org.agoncal.application.petstore.model.Item --relationshipType Many-to-One --cascadeType PERSIST ;
# Constraints
constraint-add --onProperty quantity --constraint Min --value 1 ;


#  PurchaseOrder entity
#  ############
jpa-new-entity --named PurchaseOrder --tableName purchase_order ;
jpa-new-field --named orderDate --type java.util.Date --temporalType DATE --columnName order_date --not-updatable ;
jpa-new-field --named totalWithoutVat --type java.lang.Float ;
jpa-new-field --named vatRate --type java.lang.Float --columnName vat_rate ;
jpa-new-field --named vat --type java.lang.Float ;
jpa-new-field --named totalWithVat --type java.lang.Float ;
jpa-new-field --named total --type java.lang.Float ;
# Address embeddable
jpa-new-field --named street1 --length 50 ;
jpa-new-field --named street2 ;
jpa-new-field --named city --length 50 ;
jpa-new-field --named state ;
jpa-new-field --named zipcode --columnName zip_code --length 10 ;
# Credit card embeddable
jpa-new-field --named creditCardNumber --columnName credit_card_number ;
jpa-new-field --named creditCardType --type org.agoncal.application.petstore.model.CreditCardType --columnName credit_card_type ;
jpa-new-field --named creditCardExpDate --columnName credit_card_expiry_date  ;
# Relationships
jpa-new-field --named customer --type ~.model.User --relationshipType Many-to-One ;
jpa-new-field --named orderLines --type ~.model.OrderLine --relationshipType One-to-Many ;
# Constraints
constraint-add --constraint NotNull --onProperty street1 ;
constraint-add --constraint Size --min 5 --max 50 --onProperty street1 ;
constraint-add --constraint NotNull --onProperty city ;
constraint-add --constraint Size --min 5 --max 50 --onProperty city ;
constraint-add --constraint NotNull --onProperty zipcode ;
constraint-add --constraint Size --min 1 --max 10 --onProperty zipcode ;
constraint-add --constraint NotNull --onProperty creditCardNumber ;
constraint-add --constraint Size --min 1 --max 30 --onProperty creditCardNumber ;
constraint-add --constraint NotNull --onProperty creditCardType ;
constraint-add --constraint NotNull --onProperty creditCardExpDate ;
constraint-add --constraint Size --min 5 --max 5 --onProperty creditCardExpDate ;


#  Package Vetoed
#  ############
java-new-package --named ~.model --createPackageInfo ;
# java-add-annotation --annotation javax.enterprie.inject.Vetoed --targetClass ~.model.package-info ;



#  #######################  #
#  Creates utility classes  #
#  #######################  #

#  Resource Producer
#  ############
java-new-class --named ResourcProducer --targetPackage ~.util ;

java-new-method --methodName produceFacesContext --returnType javax.faces.context.FacesContext --accessType private ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onMethod produceFacesContext ;
java-add-annotation --annotation javax.enterprise.context.RequestScoped --onMethod produceFacesContext ;

java-new-method --methodName produceHttpServletResponse --returnType javax.servlet.http.HttpServletResponse --accessType private ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onMethod produceHttpServletResponse ;
java-add-annotation --annotation javax.enterprise.context.RequestScoped --onMethod produceHttpServletResponse ;

java-new-method --methodName produceLogger --returnType java.util.logging.Logger --accessType private ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onMethod produceLogger ;
java-add-annotation --annotation javax.enterprise.context.RequestScoped --onMethod produceLogger ;


#  Logging Interceptor
#  ############
cdi-new-interceptor-binding --named Loggable --targetPackage ~.util ;
cdi-new-interceptor --named LoggingInterceptor --interceptorBinding ~.util.Loggable --targetPackage ~.util ;
java-new-field --named logger --type org.apache.logging.log4j.Logger --generateGetter=false --generateSetter=false --updateToString=false --updateToString=false ;
java-add-annotation --annotation javax.inject.Inject --onProperty logger ;


#  Exception Interceptor
#  ############
cdi-new-interceptor-binding --named CatchException --targetPackage ~.view.util ;
# TODO FORGE-2466
# cdi-new-interceptor --named CatchExceptionInterceptor --interceptorBinding ~.view.util.CatchException  --targetPackage ~.view.util ;
cdi-new-interceptor --named CatchExceptionInterceptor --interceptorBinding org.agoncal.application.cdbookstore.view.util.CatchException  --targetPackage ~.view.util ;
cdi-add-injection-point --named logger --type java.util.logging.Logger ;


#  Number generators interface and qualifier
#  ############
cdi-new-qualifier --named EightDigits --targetPackage com.pluralsight.injection.module07.util ;
cdi-new-qualifier --named ThirteenDigits --targetPackage com.pluralsight.injection.module07.util ;
cdi-new-qualifier --named Vat --targetPackage com.pluralsight.injection.module07.util ;
cdi-new-qualifier --named Discount --targetPackage com.pluralsight.injection.module07.util ;
java-new-interface --named NumberGenerator --targetPackage com.pluralsight.injection.module07.util ;
java-new-method --methodName generateNumber --returnType String --accessType public ;


#  IsbnGenerator
#  ############
cdi-new-bean --named IsbnGenerator --qualifier com.pluralsight.injection.module07.util.ThirteenDigits --targetPackage com.pluralsight.injection.module07.util ;
java-new-field --named logger --type org.apache.logging.log4j.Logger --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.inject.Inject --onProperty logger ;
java-new-field --named prefix --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.inject.Inject --onProperty prefix ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.ThirteenDigits --onProperty prefix ;
java-new-field --named postfix --type int --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.inject.Inject --onProperty postfix ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.ThirteenDigits --onProperty postfix ;
java-new-method --methodName generateNumber --returnType String --accessType public ;

#  IssnGenerator
#  ############
cdi-new-bean --named IssnGenerator --qualifier com.pluralsight.injection.module07.util.EightDigits --targetPackage com.pluralsight.injection.module07.util ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.EightDigits ;
java-new-field --named logger --type org.apache.logging.log4j.Logger --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.inject.Inject --onProperty logger ;
java-new-field --named prefix --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.inject.Inject --onProperty prefix ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.EightDigits --onProperty prefix ;
java-new-field --named postfix --type int --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.inject.Inject --onProperty postfix ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.EightDigits --onProperty postfix ;
java-new-method --methodName generateNumber --returnType String --accessType public ;

#  Number producer
#  ############
java-new-class --named NumberProducer --targetPackage com.pluralsight.injection.module07.util ;
java-new-field --named prefix1 --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty prefix1 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.ThirteenDigits --onProperty prefix1 ;
java-new-field --named postfix1 --type int --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty postfix1 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.ThirteenDigits --onProperty postfix1 ;

java-new-field --named prefix2 --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty prefix2 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.EightDigits --onProperty prefix2 ;
java-new-field --named postfix2 --type int --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty postfix2 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.EightDigits --onProperty postfix2 ;

java-new-field --named prefix3 --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty prefix3 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.ThirteenDigits --onProperty prefix3 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.EightDigits --onProperty prefix3 ;
java-add-annotation --annotation javax.enterprise.inject.Alternative --onProperty prefix3 ;

java-new-field --named postfix3 --type int --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty postfix3 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.ThirteenDigits --onProperty postfix3 ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.EightDigits --onProperty postfix3 ;
java-add-annotation --annotation javax.enterprise.inject.Alternative --onProperty postfix3 ;

java-new-field --named vatRate --type java.lang.Float --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty vat ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.Vat --onProperty vat ;
java-add-annotation --annotation javax.inject.Named --onProperty vat ;

java-new-field --named discountRate --type java.lang.Float --generateGetter=false --generateSetter=false --updateToString=false ;
java-add-annotation --annotation javax.enterprise.inject.Produces --onProperty discountRate ;
java-add-annotation --annotation com.pluralsight.injection.module07.util.Discount --onProperty discountRate ;
java-add-annotation --annotation javax.inject.Named --onProperty discountRate ;


#  #####################  #
#  Adding a Service Tier  #
#  #####################  #




#  #############################  #
#  Generates JSF beans and pages  #
#  #############################  #



#  ########################  #
#  Generates REST endpoints  #
#  ########################  #



#  #########################  #
#  Generate Arquillian tests
#  #########################  #

# JSF Beacking Beans
# ##################

# REST Endpoints
# ##############

# Services
# ##############



#  ##################  #
#  Cleans the pom.xml  #
#  ##################  #
project-remove-dependencies org.hibernate.javax.persistence:hibernate-jpa-2.1-api:jar:: ;
project-remove-dependencies javax.enterprise:cdi-api:jar:: ;
project-remove-dependencies javax.ejb:javax.ejb-api:jar:: ;
project-remove-dependencies javax.faces:javax.faces-api:jar:: ;
project-remove-dependencies javax.servlet:javax.servlet-api:jar:: ;
project-remove-dependencies javax.validation:validation-api:jar:: ;

project-remove-managed-dependencies org.hibernate.javax.persistence:hibernate-jpa-2.1-api:jar::1.0.0.Draft-16 ;
project-remove-managed-dependencies javax.enterprise:cdi-api:jar::1.1 ;
project-remove-managed-dependencies javax.ejb:javax.ejb-api:jar::3.2 ;
project-remove-managed-dependencies javax.faces:javax.faces-api:jar::2.2 ;
project-remove-managed-dependencies javax.servlet:javax.servlet-api:jar::3.1.0 ;
project-remove-managed-dependencies org.jboss.spec:jboss-javaee-6.0:pom::3.0.2.Final ;

#  Adding Java EE and Web Jars dependencies
#  ############################
project-add-dependencies org.webjars:bootstrap:2.3.2 ;
project-add-dependencies org.primefaces:primefaces:5.1 ;
project-add-dependencies org.jboss.spec:jboss-javaee-7.0:1.0.1.Final:provided:pom ;

#  Adding repositories
#  ############################
project-add-repository --named jboss-public --url https://repository.jboss.org/nexus/content/groups/public/ ;


