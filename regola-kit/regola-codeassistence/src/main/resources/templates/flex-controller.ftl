package ${controller_package}
{
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.rpc.AsyncResponder;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import org.regola.collections.RemoteCollection;
	import org.regola.model.ModelProperty;
	
	import ${model_class};
	import ${filter_class};
	
	[Bindable]
	public class ${controller_name}
	{
		public var service:RemoteObject;
		public var pattern:MateriaPattern = new MateriaPattern();
		public var modelList:RemoteCollection;

		public function init():void
		{
			modelList = new RemoteCollection(fetcher,count, pattern.pageSize);
		}
		
		private function faultHandler(event:FaultEvent,token:Object = null):void
		{
			Alert.show(event.fault.faultString, "Attenzione");
		}
		
		public function count(collection:RemoteCollection):void
		{
			var token:AsyncToken = service['countMaterie'].send(pattern);
			token.addResponder(new AsyncResponder(collection.receivedCount, faultHandler) );
		}
		
		public function fetcher(collection:RemoteCollection,  page:uint):void
		{
			pattern.currentPage = page;
			var token:AsyncToken = service['findMaterie'].send(pattern);
			token.addResponder(new AsyncResponder(collection.receivedData, faultHandler,page) );
		}
		
	}
}