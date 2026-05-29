Option Explicit

Dim shell, fso, raiz, backend, comando, url
Set shell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

raiz = fso.GetParentFolderName(WScript.ScriptFullName)
backend = fso.BuildPath(raiz, "backend")
url = "http://localhost:8080"

If Not fso.FolderExists(backend) Then
    MsgBox "A pasta backend nao foi encontrada. Verifique a estrutura do projeto.", vbCritical, "Izzy Hotel"
    WScript.Quit
End If

comando = "cmd /c cd /d """ & backend & """ && .\mvnw.cmd spring-boot:run >> target\izzy-hotel-start.log 2>>&1"

shell.Run comando, 0, False

If AguardarSistema(url, 90) Then
    shell.Run url, 1, False
Else
    MsgBox "O sistema foi iniciado, mas nao respondeu em ate 90 segundos. Verifique se o MySQL esta aberto e confira o log em backend\target\izzy-hotel-start.log.", vbExclamation, "Izzy Hotel"
End If

Function AguardarSistema(endereco, segundos)
    Dim http, inicio
    inicio = Timer

    Do
        On Error Resume Next
        Set http = CreateObject("MSXML2.ServerXMLHTTP.6.0")
        http.Open "GET", endereco, False
        http.Send

        If Err.Number = 0 Then
            If http.Status >= 200 And http.Status < 500 Then
                AguardarSistema = True
                Exit Function
            End If
        End If

        Err.Clear
        On Error GoTo 0
        WScript.Sleep 2000
    Loop While Timer - inicio < segundos

    AguardarSistema = False
End Function
