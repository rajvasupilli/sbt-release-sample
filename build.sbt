import ReleaseTransformations._
import com.typesafe.sbt.pgp

name := "sbt-release-sample"

scalaVersion := "2.12.4"

organization := "uk.co.callhandling"

moduleName := "test-release"

credentials += Credentials(Path.userHome / "sonatype.credentials")
credentials += Credentials(Path.userHome / "pgp.credentials")

useGpg := true

useGpgAgent := true

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
  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runClean,                               // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
  ReleaseStep(action = Command.process(s"""sonatypeOpen "${organization.value}" "${name.value} v${version.value}"""", _)),
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,                         // : ReleaseStep
  commitNextVersion,                      // : ReleaseStep
  pushChanges

)

releaseUseGlobalVersion := false

PgpKeys.pgpSelectPassphrase := pgpPassphrase.value orElse
  (Credentials.forHost(credentials.value, "pgp") map (_.passwd.toCharArray))
