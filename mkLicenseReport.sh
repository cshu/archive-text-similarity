mkdir -p licenses
rm -rf licenses/*
cd sse
./gradlew licenseReport
cp -pr build/reports/licenses/licenseReport.* ../licenses
cd ../licenses
find * -name 'licenseReport.*' -exec mv {} sse-{} \;
cd ../similartext
./gradlew licenseReport
cp -pr build/reports/licenses/licenseReport.* ../licenses
cd ../licenses
find * -name 'licenseReport.*' -exec mv {} similartext-{} \;
cd ../stwork
./gradlew licenseReport
cp -pr build/reports/licenses/licenseReport.* ../licenses
cd ../licenses
find * -name 'licenseReport.*' -exec mv {} stwork-{} \;
cd ../stws
./gradlew licenseReport
cp -pr build/reports/licenses/licenseReport.* ../licenses
cd ../licenses
find * -name 'licenseReport.*' -exec mv {} stws-{} \;
cd ..
cp stui/dist/stui/3rdpartylicenses.txt licenses/stui-3rdpartylicenses.txt
mkdir -p licenses/kafka_related
cp kafka_2.13-3.6.0/LICENSE licenses/kafka_related
cp kafka_2.13-3.6.0/NOTICE licenses/kafka_related
cp -pr kafka_2.13-3.6.0/licenses licenses/kafka_related
