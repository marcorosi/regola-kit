# Nell'ottica di indagare se ci siano veri vantaggi
# nell'impiego di Ruby per il livello di presentazione
# propongo questa classe che sostituisce CustomerList
#

require 'java'

include_class 'org.regola.webapp.action.plug.BasePagePlug'
include_class 'org.regola.events.Event'
include_class 'org.regola.model.Customer'
include_class 'org.regola.model.pattern.CustomerPattern'
include_class 'org.regola.webapp.action.ListPage'
include_class 'org.regola.webapp.action.plug.ListPagePlugProxy'

class CustomerList 

 def init()
    @@listPage.setPlug( ListPagePlugProxy.new(self))
    @@listPage.setFilter(CustomerPattern.new)
    @@listPage.init
    @@listPage.getEventBroker().subscribe(self, 'customer.persistence.changes')
 end

 def setListPage(listPage)
  @@listPage = listPage
 end

 def getListPage
  @@listPage
 end
 
 def onRegolaEvent(event)
  @@listPage.refresh
 end

end

CustomerList.new 

