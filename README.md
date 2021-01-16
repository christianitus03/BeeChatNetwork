# TempleNet
### World's first non-ZOGed internet alternative
###### Should probably add a license notice to this but I really don't have time, anyways it's GPL3

TempleNet is a project dedicated to creating an anonymous, encrypted, and indestructible alternative to the current internet. 
It is a fork of [BeeChatNetwork](https://github.com/beechatnetworkadmin/BeechatNetwork).
It utilizes the DigiMesh mesh network protocol to create a virtually unlimited 802.15.4 radio mesh network.

Currently, it is in a very early proof-of-concept stage. At the moment, it is only able to send 52 character chat messages.
In the near future, we'll be able to support all the current features of the normal internet, such as:
* Rich webpages.
* Live audio and video.
* Cryptocurrency transfer.
* And more!

All of this can be done using encrypted transmissions, and the network's architecture is structured to be fully
anonymous and impervious to traditional internet attacks. 

Additionally, since it is completely wireless; **it is highly resistant to EMP attacks.**

### So what's this repository then?
This is a library which will eventually be used for an easy backend for web developers. I intend for it to be
designed such that anyone familiar with backend web programming can easily pick up on the way TempleNet functions.

### What needs to be done?
1. Make it truly extensible. Currently, this is just a single shitty chat application to prove that this works. 
   I want to provide an interface for developers, one which builds upon the [base library provided by Digi](https://github.com/digidotcom/xbee-java).
   I want the API to be similar in function, design, and overall architecture to the current World Wide Web.
   The base library covers OSI layers 1, 2, and 3. This project covers layer 4 and (optionally) 5.
2. DOCUMENT! Barely any documentation. Need to fix that.
3. ADVERTISE! We need more people working on this, and more people with dongles plugged in. This is a mesh network, so even
   just plugging in an XBee dongle and letting it sit while your computer is on will help expand the network.
4. BACKUP! With the way things are going now in the US (you know what I'm talking about), It's likely this repo is going to 
   get taken down and this project investigated, even though what we are doing is totally legal. So we need to make backups
   for this project and keep it going no matter what.
5. (bonus) PYTHON! Since Digi also provides a [Python library](https://github.com/digidotcom/xbee-python), we could also
    work on getting this ported to there so that Python developers can work on it as well. 
