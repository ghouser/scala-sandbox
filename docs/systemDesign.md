# System Design
Notes on high level system design

# Scale
* **webhost** - need a hosting company
  * do they block certain regions?
  * common security? sftp, https
  * realistic features 
  * VPS vs Cloud vs Dedicated
    * VPS - virtual private servers (you get some slice of a physical server)
      * better security and customization, EC2 is an example
    * Cloud - you get a virtual and elastic resource
      * better scale, can hit higher peek performance
* **Vertical Scaling**
  * Just add more resources to a single machine (more RAM, CPU & storage)
  * Has a limit, can only go so fast w/ so much memory
  * Better software w/ treading and multi processors helps
* **Horizontal Scaling**
  * Get many cheaper machines with less resources
  * Requires a load Balancer
  * Generally machines share hard disks
    * RAID helps here, striping can let you write data anywhere and be redundant.

## Load Balancing
* When addressing a server, DNS does not return the IP of the server, but that of the load balancer
  * Load balancer can be stand-alone server
  * Load balancer can be a task of the DNS Server
* Backend servers have *private* addresses.
* Load balancing in DNS Server
  * send in a round-robin manner
    * DNS Server balancing can send to endpoint 1, 2, 3, 4, etc on a loop.
    * Can get "bad luck" with heavy activity
    * caching will force you back to the same server, creating unbalanced load if a single source repeatedly calls while others do not
* Load balancing in stand-alone
  * Generally preferred
  * balance by activity -> send to the least active server
  * balance by task -> this server is a microservice specifically doing X
    * microservices can ALSO be horizontally scaled and load balanced
* Load balancing can do weird things to sessions
  * eg -> shopping cart, added things to a cart, came back to an empty cart because the cart was on a different server
  * session state needs to be stored in common location
    * need to avoid single source of failure
    * servers could share storage
      * networked / shared file system or SQL DB
      * needs to be redundant and in sync, see replication
    * Cookies - store some info about server they used before
      * cookie can store some GUID, load balancer then stores same GUID to determine proper path
      * load balancer can set the cookie
* Preferred to us software over hardware solutions
* Load balancing across GEOGRAPHIC locations is most complex
  * generally, you need to copy resources over to spin up new location
  * talk failovers and availability zones

## Caching
* Goal is to make things faster by removing repetitive actions
  * PHP can be optimized by compiling and caching the results
* File based caching
  * eg Server side rendering in static content
    * render the HTML -> store HTML -> return HTML rather than regenerating
  * It is very fast offsetting higher disk space
  * really only works for static content
    * Netflix pre-rendering transcodes is GREAT example
* SQL query cache
* Memcache (memory cache)
  * eg play cache API
  * needs to be configured
    * set size limits as it consumes RAM
    * trash collection and clean up -> first in, first out w/ refresh
* Read heavy applications care more about caching
* How is data storage in cache
  * could be compressed - slower read, but can hold the data longer

## DNS
* I type words into a browser, browser sends those to DNS Server
  * DNS Server replies with an IP address to the _real_ server I want.
* Geography based load balancing to help failover and availability zones
  
## Replication
* Parent source
  * Potentially many child replicas
* Write redundant with Read-only replicas
  * Updates & Write go to one of many master write servers
    * data merge replicates between them
    * many tech options for this
  * Writes then propagates to many read servers
    * application can read from any read server
  * helps read only systems
  * this is becoming easier with cloud services
    * auto failover - one master can immediately take over 
    * zone redundant backups - store backups somewhere else (helps failovers, too)
    * high availability - ensures a second is always "ready to go"
* Data integrity
  * How to ensure data is up to date
  * "read committed snapshot"
  
## Partitioning
* need a key that can be used to identify how to partition
* examples:
  * single tenant databases by client
    * kafka partitions by client
  * splitting users by last name
  * splitting events by date
    * bucketing and summarizing event based data
  
# Security
* Firewall to limit only HTTPS into load balancer
  * generally can drop HTTPS inside network
  * to keep HTTPS inside network, need SSL certificates for each server
* Firewall on SQL Server to limit only ports needed (1433)

# Common Trade Offs
