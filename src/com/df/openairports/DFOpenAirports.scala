package com.df.openairports

import com.google.common.base.Splitter
import scala.collection.JavaConverters._



/*
  DFAirports application has data for Countries, Airports and Runways,
  as comma separated values in csv files.
  Application can do the following for It’s Users.

  User can:
  0. Query to get a List of All Countries.
  1. Query any Country Information by providing Country Code.
  2. Query Airports in Any Country by Providing Country Code.

  To Be Done By Learners
  3. Query Airport Information by providing Airport Reference.
  4. Query Runway Information by Providing Airport Reference.

*/
object DFOpenAirports extends App {
      println(
        """************************************
          |     WELCOME TO DFAIRPORTS APP
          |************************************
        """.stripMargin)

      println

      listAllOptions

  lazy val countries = try {
    val bufferedSource = scala.io.Source.fromFile("conf/countries.csv")
    val arrOflines = try bufferedSource.getLines.toArray finally bufferedSource.close()
    val linesStream = arrOflines.drop(1).par.toStream
    val countries = new java.util.ArrayList[Country]()
    linesStream foreach ( line => {
      val cols = Splitter.on(',').split(line).asScala.toArray
      countries.add(Country(cols(0), cols(1).stripSuffix("\"").stripPrefix("\""), cols(2).stripSuffix("\"").stripPrefix("\""), cols(3).stripSuffix("\"").stripPrefix("\""), cols(4).stripSuffix("\"").stripPrefix("\""), cols(5).stripSuffix("\"").stripPrefix("\"")))
    })
    countries.asScala.toList
  }catch {
    case e:Exception => e.printStackTrace
    List[Country]()
  }

  lazy val airports = try {
    val bufferedSource = scala.io.Source.fromFile("conf/airports.csv")
    val arrOflines = try bufferedSource.getLines.toArray finally bufferedSource.close()
    val linesStream = arrOflines.drop(1).par.toStream

    val airports = new java.util.ArrayList[Airport]()
    linesStream foreach ( line => {
      val cols = Splitter.on(',').split(line).asScala.toArray
      if(cols.length == 18)
        airports.add(Airport(cols(0), cols(1).stripSuffix("\"").stripPrefix("\""), cols(2).stripSuffix("\"").stripPrefix("\""), cols(3).stripSuffix("\"").stripPrefix("\""), cols(4), cols(5), cols(6), cols(7), cols(8).stripSuffix("\"").stripPrefix("\""), cols(9).stripSuffix("\"").stripPrefix("\""), cols(10).stripSuffix("\"").stripPrefix("\""), cols(11), cols(12), cols(13), cols(14), cols(15), cols(16), cols(17)))
    })

    airports.asScala.toList
  } catch {
    case e:Exception => e.printStackTrace
      List[Airport]()
  }
  //for runway

  lazy val runways = try {
    val bufferedSource = scala.io.Source.fromFile("conf/runways.csv")
    val arrOflines = try bufferedSource.getLines.toArray finally bufferedSource.close()
    val linesStream = arrOflines.drop(1).par.toStream

    val runways = new java.util.ArrayList[Runway]()
    linesStream foreach ( line => {
      val cols = Splitter.on(',').split(line).asScala.toArray
      if(cols.length == 20)
        runways.add(Runway(cols(0), cols(1), cols(2).stripSuffix("\"").stripPrefix("\""),
          cols(3).stripSuffix("\"").stripPrefix("\""), cols(4), cols(5), cols(6), cols(7),
          cols(8).stripSuffix("\"").stripPrefix("\""), cols(9).stripSuffix("\"").stripPrefix("\""),
          cols(10).stripSuffix("\"").stripPrefix("\""), cols(11), cols(12), cols(13), cols(14), cols(15),
          cols(16), cols(17), cols(18), cols(19)))
    })

    runways.asScala.toList
  } catch {
    case e:Exception => e.printStackTrace
      List[Runway]()
  }

  def listAllCountries(countries: List[Country]) = {
      countries foreach(country => {
        println(country)
      })
  }

  def countryInformation(countries: List[Country]) = {
      println("Please Enter Country Code: ")
      val input = scala.io.StdIn.readLine()
      val country = countries.filter(country => {country.code == input}).headOption
      if(country.isDefined && country.get != null) println(country.get) else println("No Country Found with given Input")
  }

  def airportInformation(airports: List[Airport]) = {
      println("Please Enter Country Code to Get Airports")
      val input = scala.io.StdIn.readLine()
      val airportsList = airports.filter(airport => airport.isoCountry == input)
      if(airportsList.headOption.isDefined && airportsList.head != null) show(airportsList) else println("No Airports Found with given Input")
      def show(airportsList: List[Airport]) = {
          airportsList foreach(airportInformation => println(airportInformation))
      }
  }

  def runwayInformation(runways: List[Runway]) = {
    println("Please Enter Airports Reference")
    val input = scala.io.StdIn.readLine()
    val runwaysList = runways.filter(runways => runways.airport_ref == input)
    if(runwaysList.headOption.isDefined && runwaysList.head != null) show(runwaysList) else println("No Runways Found with given Input")
    def show(runwaysList: List[Runway]) = {
      runwaysList foreach(runwayInformation => println(runwayInformation))
    }
  }

  def listAllOptions: Unit = {
    println(
      """Please Choose any Option to Proceed:
        |0. List All Countries
        |1. Country Information By Country Code
        |2. Airports within a Country By Country Code
        |3. Runway Information
        |4. Exit
      """.stripMargin)
    println("-------------------------------------")

    print("Option :: ")

    scala.io.StdIn.readInt() match {
      case 0 => listAllCountries(countries); println; listAllOptions
      case 1 => countryInformation(countries); println; listAllOptions
      case 2 => airportInformation(airports); println; listAllOptions
      case 3 => runwayInformation(runways); println(); listAllOptions
      case 4 => println("Thank You.")
      case _ => println(s"Seems You've chosen the wrong Option."); println; listAllOptions

    }
  }

}

case class Country(id: String, code: String, name: String, continent: String, wikipedia_link: String, keywords: String)

case class Airport(id: String, ident: String, airportType: String, name: String,latitudeDeg: String,longitudeDeg: String,
                   elevationFt: String, continent: String, isoCountry: String, isoRegion: String, municipality: String,
                   scheduledService: String, gpsCode: String, iataCode: String, localCode: String, homeLink: String,
                   wikipedia_link: String, keywords: String)
//"id","airport_ref","airport_ident","length_ft","width_ft",
// "surface","lighted","closed","le_ident","le_latitude_deg","le_longitude_deg",
// "le_elevation_ft","le_heading_degT","le_displaced_threshold_ft","he_ident",
// "he_latitude_deg","he_longitude_deg","he_elevation_ft","he_heading_degT","he_displaced_threshold_ft",

case class Runway(id: String, airport_ref: String, airport_ident: String,
                  length_ft: String, width_ft: String, surface: String,
                   lighted: String, closed: String, le_ident: String,
                  le_latitude_deg: String, le_longitude_deg: String, le_elevation_ft: String,
                  le_heading_degT: String, le_displaced_threshold_ft: String, he_ident: String,
                  he_latitude_deg: String, he_longitude_deg: String, he_elevation_ft: String,
                  he_heading_degT: String, he_displaced_threshold_ft: String)