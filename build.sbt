name := "GraphicalGame"
version := "1.0"
scalaVersion := "2.12.10"
run / fork := true
run / connectInput := true

libraryDependencies ++= Seq(
//	"org.scalafx" %% "scalafx" % "8.0.192-R14",
	"org.scalafx" %% "scalafx" % "12.0.2-R18",
	"com.novocode" % "junit-interface" % "0.11" % Test,
	"org.scalactic" %% "scalactic" % "3.0.8",
	"org.scalatest" %% "scalatest" % "3.0.8" % "test"
)
