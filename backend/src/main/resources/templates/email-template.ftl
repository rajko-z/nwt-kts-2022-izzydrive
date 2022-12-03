<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="email-template-style.css">
</head>
<body>
<table style="  border: solid thin #696F91;
                border-radius: 5px;
                width: 400px;
                height: 500px;
                border-spacing:0;
                border-collapse: collapse;
                font-family: Inter"
        cellspacing="0"
       cellpadding="0">

    <tr  style="    height: 10%;
                    background-color: #191A23;
                    padding-left: 40%;">
        <td>
            <p style="color: #E9EAEF;font-size: large;text-align:center;">IzzyDrive</p>
        </td>
    </tr>
    <tr>
        <td>
            <p style="color: #191A23;font-size: xx-large;
                font-weight: bold;
                text-align: center;">
                Welcome to IzzyDrive
            </p>
            <p style="color: #4C506C;font-size: 20px;
                    font-style: italic;
                    font-weight: bold;
                    text-align: center;
                    padding-left: 10%;
                    padding-right: 10%;">
                Thank you for using our application.&#x1F49C;</p>
            <div style="color: #4C506C;
                        background-color: #D9DBF1;
                        font-size: 22px;
                        font-weight: bold;
                        padding: 10%;
                        text-align: center;
                        margin-left: 10%;
                        margin-right: 10%">
                <p>
                    To make your account active, please click on this
                    <a href="${token}">link</a>
                </p>
            </div>
        </td>
    </tr>
    <tr>
        <td>
        </td>
    </tr>
</table>
</body>

</html>