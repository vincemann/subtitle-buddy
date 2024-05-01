[Setup]
AppName=Subtitle Buddy
AppVersion=1.1.0
DefaultDirName={pf}\SubtitleBuddy
DefaultGroupName=com.github.vincemann
OutputDir=output
OutputBaseFilename=subtitle-buddy-installer
Compression=lzma
SolidCompression=yes

[Files]
Source: "application.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "jre/*"; DestDir: "{app}\jre"; Flags: recursesubdirs ignoreversion
Source: "lib/*"; DestDir: "{app}\lib"; Flags: recursesubdirs ignoreversion
Source: "start.bat"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\Subtitle Buddy"; Filename: "{app}\jre\bin\java.exe"; Parameters: "-jar {app}\application.jar"

[Run]
Filename: "{app}\start.bat"; Description: "Launch Subtitle Buddy"; Flags: nowait postinstall skipifsilent