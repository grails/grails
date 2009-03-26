#!/bin/bash

name='site'
appName='grails'
prevVersion='3.1'
version='3.2'
url='grails.org'
sourceRoot='/home/grails-j2ee'
environment='prod'
deployDir='beta'

if [ "$1" ]
then
    environment="$1"
fi
if [ $environment == "prod" ];
then
    deployDir='www'
fi

# either beta-j2ee or www-j2ee
user=$deployDir-j2ee
echo "user is $user"

deployLoc="/opt/j2ee/domains/$url/$deployDir/webapps/$appName"
echo "deployment location is $deployLoc"
sourceLoc="$sourceRoot/apps/$url"
echo "sourceLoc is $sourceLoc"
currentDir=`pwd`

cd $sourceRoot/apps/grails
echo "git pull..."
git pull

cd $sourceLoc

echo "Packaging grails war in $sourceLoc ..."
# ~/grails-1.1/bin/grails clean
# rm *.war
# ~/grails-1.1/bin/grails prod war

cdtDate=`TZ=CST date +"%Y%m%d-%H%M"`
newFolder=$name-$version-$cdtDate
echo "Creating new deployment to folder: $newFolder..."
warFolder=$deployLoc/$newFolder/exploded_war
sudo $user mkdir -p $warFolder
sudo $user chown -R www-j2ee:www-j2ee $deployLoc/$newFolder
cd $warFolder
echo "Exploding war..."
jar xf $sourceLoc/*.war

echo "Copying over images from previous version..."
sudo $user cp $deployLoc/$name-$prevVersion/exploded_war/images/* $deployLoc/$newFolder/images

echo "Replacing current directory..."
cd $deployLoc
if (test -e "current"); then
    sudo $user rm "current"
fi
sudo $user ln -s $newFolder current

echo "$deployLoc/$newFolder is now ready to be deployed"
cd $currentDir