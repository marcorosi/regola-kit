## RFC 2 ##
# Property expression language proposal #

ModelPattern's annotations have to refer to the model property upon which the filter will be applied. If the model property is not present the ModelPattern field or property name is used by default.

This property reference is a property (or navigation) path with a simple and natural syntax.

We are using HSQLDB sample data for the examples.

  1. The _**dot**_ is used as the usual **path step separator**.
> > Examples:
    * Root entity simple property (Customer):
```
firstName
```
    * Embedded component property (Customer):
```
address.street
```
    * Association property single-valued ("to one" relation: Invoice to Customer):
```
customer.lastName
```
    * Association property single-valued ("to one" relation: Item to Product):
```
product.name
```
  1. _Multi-valued association traversal_ ("to many" relation) is indicated with _**postfixed square brackets**_ "[.md](.md)"
> > Examples:
```
invoices[]
```


Complete example from entity Customer to Invoice then to Item and finally to Product:
```
invoices[].items[].product.name
```


## Query language comparison ##
_(to be moved in another page)_

_**Filtered property**_ (from entity Customer): `@NotEquals("invoices[].items[].product.name")`
|HQL|`select distinct e1 from Customer e1 join e1.invoices e2 join e2.items e3 where e3.product.name != :p1`|
|:--|:------------------------------------------------------------------------------------------------------|
|JPQL|`select distinct e1 from Customer e1 join e1.invoices e2 join e2.items e3 where e3.product.name <> :p1`|
|JDOQL|`select from org.regola.model.Customer where invoices.contains(e2) && e2.items.contains(e3) && e3.product.name != :p1`|

_**Filtered property**_ (from entity Customer): `@Equals("address.street")`

|HQL|`select distinct e1 from Customer e1 where e1.address.street = :p1`|
|:--|:------------------------------------------------------------------|
|JPQL|`select distinct e1 from Customer e1 where e1.address.street = :p1`|
|JDOQL|`select from org.regola.model.Customer where address.street == :p1`|

_**Count query**_
|HQL|`select count(distinct e1) from ...`|
|:--|:-----------------------------------|
|JPQL|`select count(distinct e1) from ...`|
|JDOQL|` `|

_**Ordering**_
|HQL|`... where ... order by e1.lastName, e3.product.name desc`|
|:--|:---------------------------------------------------------|
|JPQL|`... where ... order by e1.lastName, e3.product.name desc`|
|JDOQL|` `|

_**Limits**_
|HQL|`N/A (must use query api)`|
|:--|:-------------------------|
|JPQL|`N/A (must use query api)`|
|JDOQL|` `|