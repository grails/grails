The Dispatcher plugin has a default proxy script (dispatcher.php), the code of this PHP script is simple:

<?php
if (array_key_exists('uri', $_GET)) {
  @readfile($_GET['uri']);
}
?>

And the functionality is to load a remote script and send the content to the client, you can add security check to this script. By Default this script is in the same DIR of the current page, be aware and use a correct path in case you use it in a multi-level website.