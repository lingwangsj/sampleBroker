import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { DomainMySuffix } from './domain-my-suffix.model';
import { DomainMySuffixService } from './domain-my-suffix.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-domain-my-suffix',
    templateUrl: './domain-my-suffix.component.html'
})
export class DomainMySuffixComponent implements OnInit, OnDestroy {
domains: DomainMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private domainService: DomainMySuffixService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.domainService.query().subscribe(
            (res: ResponseWrapper) => {
                this.domains = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDomains();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: DomainMySuffix) {
        return item.id;
    }
    registerChangeInDomains() {
        this.eventSubscriber = this.eventManager.subscribe('domainListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
