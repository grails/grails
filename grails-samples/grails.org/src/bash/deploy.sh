#!/bin/bash

name='site'
appName='grails'
prevVersion='3.1'
version='3.2'
url='grails.org'
sourceRoot='/home/grails-j2ee'
deployDir='beta'

if [ "$1" ]
then
    deployDir="$1"
fi

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
~/grails-1.1.1/bin/grails clean
rm *.war
~/grails-1.1.1/bin/grails prod war

cdtDate=`TZ=CST date +"%Y%m%d-%H%M"`
newFolder=$name-$version-$cdtDate
echo "Creating new deployment to folder: $newFolder..."
warFolder=$deployLoc/$newFolder/exploded_war
mkdir -p $warFolder

# chown -R www-j2ee:www-j2ee $deployLoc/$newFolder
cd $warFolder
echo "Exploding war..."
jar xf $sourceLoc/*.war

echo "Copying over images from previous version..."
cp -r $deployLoc/$name-$prevVersion/exploded_war/images/* $deployLoc/$newFolder/exploded_war/images

echo "Replacing current directory..."
cd $deployLoc
rm previous
mv current previous
ln -s $newFolder current

echo "$deployLoc/$newFolder is now ready to be deployed"
cd $currentDir