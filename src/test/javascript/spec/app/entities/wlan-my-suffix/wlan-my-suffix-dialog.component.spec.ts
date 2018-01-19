/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanMySuffixDialogComponent } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix-dialog.component';
import { WlanMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.service';
import { WlanMySuffix } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.model';
import { ZoneMySuffixService } from '../../../../../../main/webapp/app/entities/zone-my-suffix';
import { WlanGroupMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix';

describe('Component Tests', () => {

    describe('WlanMySuffix Management Dialog Component', () => {
        let comp: WlanMySuffixDialogComponent;
        let fixture: ComponentFixture<WlanMySuffixDialogComponent>;
        let service: WlanMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanMySuffixDialogComponent],
                providers: [
                    ZoneMySuffixService,
                    WlanGroupMySuffixService,
                    WlanMySuffixService
                ]
            })
            .overrideTemplate(WlanMySuffixDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanMySuffixDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new WlanMySuffix(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.wlan = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wlanListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new WlanMySuffix();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.wlan = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wlanListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
