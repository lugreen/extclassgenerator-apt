/**
 * Generated Time:Fri Jun 01 17:06:41 GMT+08:00 2018
 */
Ext.define("MyApp.Book",
{
  extend : "Ext.data.Model",
  uses : [ "MyApp.Author" ],
  requires : [ "Ext.data.proxy.Direct", "Ext.data.identifier.Uuid" ],
  identifier : "uuid",
  idProperty : "isbn2",
  versionProperty : "version2",
  clientIdProperty : "clientId2",
  fields : [ {
    name : "isbn2",
    type : "string"
  }, {
    name : "version2",
    type : "string"
  }, {
    name : "clientId2",
    type : "string"
  }, {
    name : "title",
    type : "string"
  }, {
    name : "publisher",
    type : "string"
  }, {
    name : "isbn",
    type : "string"
  }, {
    name : "publishDate",
    type : "date",
    dateFormat : "d-m-Y"
  }, {
    name : "numberOfPages",
    type : "integer"
  }, {
    name : "read",
    type : "boolean"
  }, {
    name : "additionalProperty2",
    type : "string"
  }, {
    name : "additionalProperty1",
    type : "integer"
  } ],
  associations : [ {
    type : "hasMany",
    model : "MyApp.Author",
    associationKey : "authors",
    foreignKey : "book_id",
    primaryKey : "isbn2",
    autoLoad : true,
    name : "authors"
  } ],
  proxy : {
    type : "direct",
    idParam : "isbn2",
    writer : {
      writeAllFields : true,
      clientIdProperty : "clientId2"
    }
  }
});