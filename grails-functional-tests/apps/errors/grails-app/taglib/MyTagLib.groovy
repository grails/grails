class MyTagLib {

   static namespace = 'jeff'

   def doit = { attrs ->
       out << "some foo ${fooo}"
   }
}