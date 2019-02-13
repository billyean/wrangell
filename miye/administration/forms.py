from django import forms


class AddServiceForm(forms.Form):
    name = forms.CharField()
    slug = forms.SlugField()
    minutes = forms.CharField()
    rate = forms.DecimalField