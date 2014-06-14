
SSH Tunneling (Port Forwarding)
===============================

Version 0.1

Copyright &copy; 2013 Willy du Preez

2013-11-20

## Revision History

Version     | Date        | Author         | Description
:---------: | ----------- | -------------- | ---------------------------
0.1         | 2013-11-20  | Willy du Preez | Created

> **Secure Shell Tunneling Tutorial**

>This tutorial provides on overview of creating an SSH session for local, remote and dynamic port forwarding.

## Table of Contents

[TOC]

## Introduction

SSH tunneling consists of creating an encrypted tunnel through an SSH protocol connection. The address or port number of a packet is translated to a new destination, and the packet is then forwarded according to the routing table.

Common uses are:

 - to provide a secure path through an untrusted network, i.e. to transfer unencrypted traffic over a network through an encrypted channel
 - to bypass firewalls that prohibit certain Internet services, or to wrap a communication protocol that is blocked by a firewall inside a protocol that is allowed through the firewall

>**NOTE:** Using these techniques may violate your company or organization's AUP (Acceptable Use Policy). If you are unsure, it is safer to check with your Network / System Administrator first.

## Local Port Forwarding (Tunneling)

### Overview

Configuring an SSH client to forward traffic to a specified local port over an SSH connection to a port on a remote machine.

![Local port forwarding diagram][1]

### Command Syntax

The command syntax:

>ssh &lt;user&gt;@&lt;hostname&gt; -L &lt;bind_address&gt;:&lt;localport&gt;:&lt;host&gt;:&lt;hostport&gt;

Where:

 - user - the user on the SSH server
 - hostname - the SSH server that will be used for tunneling
 - -L - local port forwarding parameters
 - bind_address - the local address to bind to. Omit or use * to indicate bind to all interfaces
 - localport - the local port
 - host - remote server to forward to
 - hostport - port on the remote server to forward to

Additional parameters:

 - -f - sends the SSH process to the background just before command execution
 - -N - do not execute a remote command. Does not create a shell

### Examples

```
# Establishes an SSH connection to 10.10.1.5, and forwards all traffic
# sent to port 8080 on all local interfaces over the SSH connection to
# port 80 on host 10.15.1.10.
ssh user@10.10.1.5 -L 8080:10.15.1.10:80

# As the above example, but only bind to the localhost interface. Only
# traffic sent to port 8080 on localhost is sent over the SSH connection
# and forwarded.
ssh user@10.10.1.5 -L localhost:8080:10.15.1.10:80

# As the first example, but a shell is not created. Useful for port
# forwarding.
ssh user@10.10.1.5 -L 8080:10.15.1.10:80 -N

# As the above example. In addition to no shell being created, the SSH
# process is no also sent to the background.
ssh user@10.10.1.5 -L 8080:10.15.1.10:80 -N -f
```

## Remote Port Forwarding (Reverse Tunneling)

### Overview


### Command Syntax


### Examples


## Dynamic Port Forwarding


### Overview


### Command Syntax


### Examples


  [1]: ssh-tunneling-local-port-forwarding-diagram.png "Local port forwarding diagram"