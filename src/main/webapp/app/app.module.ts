import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ngx-webstorage';

import { SampleBrokerSharedModule, UserRouteAccessService } from './shared';
import { SampleBrokerAppRoutingModule} from './app-routing.module';
import { SampleBrokerHomeModule } from './home/home.module';
import { SampleBrokerAdminModule } from './admin/admin.module';
import { SampleBrokerAccountModule } from './account/account.module';
import { SampleBrokerEntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        SampleBrokerAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        SampleBrokerSharedModule,
        SampleBrokerHomeModule,
        SampleBrokerAdminModule,
        SampleBrokerAccountModule,
        SampleBrokerEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class SampleBrokerAppModule {}
