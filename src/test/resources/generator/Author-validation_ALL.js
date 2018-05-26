Ext.define("MyApp.Author",
{
  extend : "MyApp.model.Base",
  uses : [ "MyApp.Book" ],
  requires : [ "Ext.data.proxy.Direct", "Ext.data.identifier.Sequential" ],
  identifier : "sequential",
  versionProperty : "version",
  clientIdProperty : "clientId",
  fields : [ {
    name : "id",
    type : "string"
  }, {
    name : "clientId",
    type : "string"
  }, {
    name : "version",
    type : "string"
  }, {
    name : "title",
    type : "string",
    defaultValue : "Mr."
  }, {
    name : "firstName",
    type : "string",
    defaultValue : undefined
  }, {
    name : "lastName",
    type : "string",
    convert : null
  }, {
    name : "book_id",
    type : "integer"
  } ],
  associations : [ {
    type : "belongsTo",
    model : "MyApp.Book",
    associationKey : "book",
    foreignKey : "book_id",
    primaryKey : "isbn2",
    setterName : "setBook",
    getterName : "getBook"
  } ],
  proxy : {
    type : "direct",
    writer : {
      writeAllFields : true,
      clientIdProperty : "clientId"
    }
  }
});