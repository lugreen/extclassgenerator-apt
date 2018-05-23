Ext.define("App.PartialApi",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct" ],
  fields : [ ],
  proxy : {
    type : "direct",
    api : {
      read : read,
      destroy : destroy
    },
    writer : {
      writeAllFields : true
    }
  }
});