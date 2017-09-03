name := """play-scala-slick-example"""

version := "2.6.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" %  "3.0.0-M5"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0-M5"

libraryDependencies ++= Seq( 
  "org.webjars" %% "webjars-play" % "2.6.1",
  "org.webjars" % "bootstrap" % "3.3.7-1",
  "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41"   
)


libraryDependencies += "com.h2database" % "h2" % "1.4.194" % Test
libraryDependencies += specs2 % Test

/* config for testing */
javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

  

