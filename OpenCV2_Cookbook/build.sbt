name := "opencv2-cookbook"
organization := "javacv.examples"

val javacppVersion = "1.0-SNAPSHOT"
version := javacppVersion
scalaVersion := "2.11.6"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xlint")

// Disable some sources while porting to OpenCV 3.
excludeFilter in unmanagedSources := HiddenFileFilter ||
  "*Ex3ComputeFundamentalMatrix*" || "*Ex4MatchingUsingSampleConsensus*" || "*Ex5Homography*" ||
  "*RobustMatcher*" || "*MatcherUtil*" || "*Ex2CalibrateCamera*"

// Some dependencies like `javacpp` are packaged with maven-plugin packaging
classpathTypes += "maven-plugin"

// Platform classifier for native library dependencies
val platform = org.bytedeco.javacpp.Loader.getPlatform
// Libraries with native dependencies
val bytedecoPresetLibs = Seq("opencv" -> s"3.0.0-$javacppVersion").flatMap {
  case (lib, ver) => Seq(
    // Add both: dependency and its native binaries for the current `platform`
    "org.bytedeco.javacpp-presets" % lib % ver,
    "org.bytedeco.javacpp-presets" % lib % ver classifier platform
  )
}

libraryDependencies ++= Seq(
  "org.bytedeco"            % "javacpp"         % javacppVersion,
  "org.bytedeco"            % "javacv"          % javacppVersion,
  "org.scala-lang.modules" %% "scala-swing"     % "1.0.2",
  "junit"                   % "junit"           % "4.12" % "test",
  "com.novocode"            % "junit-interface" % "0.11" % "test"
) ++ bytedecoPresetLibs

// Used for testing local builds and snapshots of JavaCPP/JavaCV
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  // Use local maven repo for local javacv builds
  "Local Maven Repository" at "file:///" + Path.userHome.absolutePath + "/.m2/repository"
)

autoCompilerPlugins := true

// fork a new JVM for 'run' and 'test:run'
fork := true
// add a JVM option to use when forking a JVM for 'run'
javaOptions += "-Xmx1G"

// Set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => "sbt:" + Project.extract(state).currentRef.project + "> " }