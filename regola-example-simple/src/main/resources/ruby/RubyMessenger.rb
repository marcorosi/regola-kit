require 'java'

include_class 'it.kion.regola.service.UniboDsa'

class RubyMessenger < UniboDsa

 def setMessage(message)
  @@message = message
 end

 def getMessage
  @@message
 end
 
end

RubyMessenger.new
