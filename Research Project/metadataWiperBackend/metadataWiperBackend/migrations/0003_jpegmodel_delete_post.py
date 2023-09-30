# Generated by Django 4.2.5 on 2023-09-17 17:52

from django.db import migrations, models
import metadataWiperBackend.models


class Migration(migrations.Migration):

    dependencies = [
        ('metadataWiperBackend', '0002_docxmodel_excelmodel_pdfmodel'),
    ]

    operations = [
        migrations.CreateModel(
            name='JPEGModel',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=100)),
                ('content', models.TextField()),
                ('image', models.ImageField(upload_to=metadataWiperBackend.models.upload_to)),
            ],
        ),
        migrations.DeleteModel(
            name='Post',
        ),
    ]