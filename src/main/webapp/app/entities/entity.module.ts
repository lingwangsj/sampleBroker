import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SampleBrokerDomainMySuffixModule } from './domain-my-suffix/domain-my-suffix.module';
import { SampleBrokerZoneMySuffixModule } from './zone-my-suffix/zone-my-suffix.module';
import { SampleBrokerWlanMySuffixModule } from './wlan-my-suffix/wlan-my-suffix.module';
import { SampleBrokerWlanGroupMySuffixModule } from './wlan-group-my-suffix/wlan-group-my-suffix.module';
import { SampleBrokerAPMySuffixModule } from './ap-my-suffix/ap-my-suffix.module';
import { SampleBrokerAPGroupMySuffixModule } from './ap-group-my-suffix/ap-group-my-suffix.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SampleBrokerDomainMySuffixModule,
        SampleBrokerZoneMySuffixModule,
        SampleBrokerWlanMySuffixModule,
        SampleBrokerWlanGroupMySuffixModule,
        SampleBrokerAPMySuffixModule,
        SampleBrokerAPGroupMySuffixModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleBrokerEntityModule {}
