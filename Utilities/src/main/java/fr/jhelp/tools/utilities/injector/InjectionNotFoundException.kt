package fr.jhelp.tools.utilities.injector

class InjectionNotFoundException(className:String) : Exception("No Injection found for : $className")
