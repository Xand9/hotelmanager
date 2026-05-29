Option Explicit

Dim shell, fso, raiz, desktop, atalho, destino
Set shell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

raiz = fso.GetParentFolderName(WScript.ScriptFullName)
destino = fso.BuildPath(raiz, "Iniciar Izzy Hotel.vbs")

If Not fso.FileExists(destino) Then
    MsgBox "O arquivo Iniciar Izzy Hotel.vbs nao foi encontrado.", vbCritical, "Izzy Hotel"
    WScript.Quit
End If

desktop = shell.SpecialFolders("Desktop")
Set atalho = shell.CreateShortcut(fso.BuildPath(desktop, "Izzy Hotel.lnk"))
atalho.TargetPath = destino
atalho.WorkingDirectory = raiz
atalho.Description = "Iniciar sistema Izzy Hotel"
atalho.Save

MsgBox "Atalho criado na area de trabalho.", vbInformation, "Izzy Hotel"
