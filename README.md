# 3LittleThings-backend
www.3littlethings.top  

# バックエンド部分<br>
言語:Java(Servlet) jdk11<br>
アプリサーバー：Tomcat9<br>
Webサーバ:Nginx<br>
RDBMS:mysql<br>
JDBC接続プール：alibaba Druid<br>
クラウドサーバ：alibaba Cloud centOS7.9<br>
ビルドツール：Maven<br>
IDE:IntelliJ IDEA  /
SSL証明書：Let’s Encrypt  /

重要:exclamation:  /
ローカル開発とクラウド上のフロントエンドとバックエンドの通信方法が違います。  /
ソースコードはクラウド上と同じです。  /
ローカルでは、ViteからTomcatにリクエストを送ります。CORSの対策として、CorsFilter.javaを追加し、web.xmlでfilterを設定します。  /
クラウド上では、Nginxは静的なコンテンツを処理し、動的なコンテンツはTomcatに転送されます。  /

Gmail APIの使い方:  /
Googleのサービスアカウントがお金をかけなければならないらしいので、今回は手間のかかる方法を使います。  /
目標: 3littlethingsdiary@gmail.comから自動的にユーザーに検証メールを送ります。  /
まず、このメールアドレスの許可を得る必要があります。  /
Google OAuth2.0に対して、2つのファイルが必要です。  /
1つ目: https://console.cloud.google.com/apis/ を通してcredentials.jsonを取得します。このファイルはアプリケーションとGoogle APIの間で通信するために必要です。  /
2つ目: コードを実行して、Google OAuth 2.0の画面で3littlethingsdiary@gmail.comにログインして許可を与えます。そうすると、storedTokenが自動的に生成されます。このTokenが有効期限内であれば、自動的にメールを送ることができます。  /

SSL証明書:  /
Linuxのターミナルで取得してNginxに配置します。  /
