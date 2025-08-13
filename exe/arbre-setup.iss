; Script Inno Setup pour Arbre CLI
[Setup]
AppName=Arbre
AppVersion=2.0.0
AppPublisher=François SP
AppPublisherURL=https://github.com/Francois389/Arbre
AppSupportURL=https://github.com/Francois389/Arbre
AppUpdatesURL=https://github.com/Francois389/Arbre/releases
DefaultDirName={pf}\Arbre
DefaultGroupName=Arbre
PrivilegesRequired=admin
ChangesEnvironment=yes
AllowNoIcons=yes
InfoAfterFile=E:\projet\Arbre\README.md
OutputDir=E:\projet\Arbre\exe
OutputBaseFilename=Arbre-Setup-v2.0.0
Compression=lzma
SolidCompression=yes
WizardStyle=modern
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "E:\projet\Arbre\exe\arbre.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "E:\projet\Arbre\build\libs\arbre.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "E:\projet\Arbre\README.md"; DestDir: "{app}"; Flags: ignoreversion

[Registry]
Root: HKLM; Subkey: "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: expandsz; ValueName: "PATH"; ValueData: "{olddata};{app}"; Check: NeedsAddPath('{app}')

[Code]
function NeedsAddPath(Param: string): boolean;
var
  OrigPath: string;
begin
  if not RegQueryStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment', 'PATH', OrigPath) then
  begin
    Result := True;
    exit;
  end;
  Result := Pos(';' + Param + ';', ';' + OrigPath + ';') = 0;
end;

function CheckJavaInstalled(): Boolean;
var
  ErrorCode: Integer;
begin
  Result := Exec('java', '-version', '', SW_HIDE, ewWaitUntilTerminated, ErrorCode);
  if not Result then
  begin
    MsgBox('Java n''est pas installé ou n''est pas dans le PATH.' + #13#10 + 'Veuillez installer Java pour utiliser Arbre.' + #13#10 + 'Téléchargez Java depuis : https://jdk.java.net/', mbError, MB_OK);
  end;
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    CheckJavaInstalled();
    // Les variables d'environnement seront mises à jour au redémarrage du terminal
    // Grâce à ChangesEnvironment=yes dans [Setup]
  end;
end;