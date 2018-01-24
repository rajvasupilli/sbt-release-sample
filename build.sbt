
name := "sbt-release-sample"

scalaVersion := "2.12.4"

organization := "uk.co.callhandling"

moduleName := "test-release"

version := ".01.01-SNAPSHOT"

credentials += Credentials(Path.userHome / "pgp.credentials")

credentials += Credentials(Path.userHome / "sonatype.credentials")


useGpg := true

pgpSecretRing := Path.userHome / ".gnupg/secring.gpg"

pgpPublicRing := Path.userHome / ".gnupg/pubring.gpg"

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}



pomIncludeRepository := { (repo: MavenRepository) =>
  println(repo.root)
  repo.root.startsWith("file:")
}

licenses := Seq("Apache 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/geekminer/sbt-release-sample"))

scmInfo := homepage.value.map(url => ScmInfo(
  url,
  "scm:git@github.com:geekminer/sbt-release-sample.git"
))

developers := List(
  Developer(
    id = "geekbytes.0xff",
    name = "mts.manu",
    email = "0xff@geekbytes.io",
    url = url("http://geekbytes.io")
  )
)

publishMavenStyle := true

publishArtifact in Test := false

releaseProcess := Seq[ReleaseStep](
  ReleaseStep(action = Command.process(s"""sonatypeOpen "${organization.value}" "${name.value} v${version.value}"""", _)),

  ReleaseStep(action = Command.process("publishSigned", _))
)
