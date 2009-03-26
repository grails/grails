#!/bin/bash

name='site'
appName='grails'
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

deployLoc="/opt/j2ee/domains/$url/$deployDir/webapps/$appName"
echo "deployment location is $deployLoc"
sourceLoc="$sourceRoot/apps/$url"
echo "sourceLoc is $sourceLoc"
currentDir=`pwd`

cd $sourceLoc

echo "Updating SVN..."

svn update

echo "Packaging grails war in $sourceLoc ..."
grails clean
rm *.war
grails prod war

cdtDate=`TZ=CST date +"%Y%m%d-%H%M"`
newFolder=$name-$version-$cdtDate
echo "Creating new deployment to folder: $newFolder..."
warFolder=$deployLoc/$newFolder/exploded_war
mkdir -p $warFolder
cd $warFolder
echo "Exploding war..."
jar xf $sourceLoc/*.war

echo "Replacing current directory..."
cd $deployLoc
if (test -e "current"); then
    rm "current"
fi
ln -s $newFolder current

echo "$deployLoc/$newFolder is now ready to be deployed"
cd $currentDir
